package org.spsochnev.vacancies.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.spsochnev.vacancies.config.UserEligibilityMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, UserEligibilityMessage> kafkaTemplate;

    @Value(value = "${vacancy.kafka.topic.eligibility}")
    private String userEligibilityTopicName;

    public List<PaymentDTO> getPaymentHistoryByUsername(String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        return paymentRepository.findAllByUser(user).stream().
                map(PaymentDTO::new).
                collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserBalanceDTO processUserPayment(String username, BigDecimal amount) {
        User user = userService.getUserByUsernameOrThrow(username);
        Payment payment = new Payment(user, LocalDateTime.now(), amount);
        userService.topUpUserBalance(user, amount);
        paymentRepository.save(payment);
        if (user.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            UserEligibilityMessage message = new UserEligibilityMessage(user.getUsername(), true);
            ListenableFuture<SendResult<String, UserEligibilityMessage>> future =
                    kafkaTemplate.send(userEligibilityTopicName, message);

            future.addCallback(new ListenableFutureCallback<>() {

                @Override
                public void onSuccess(SendResult<String, UserEligibilityMessage> result) {
                    LOG.info("Sent {} with offset=[{}]", message, result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOG.error("Unable to send message due to: {}", ex.getMessage());
                }
            });
        }
        return new UserBalanceDTO(user);
    }

}
