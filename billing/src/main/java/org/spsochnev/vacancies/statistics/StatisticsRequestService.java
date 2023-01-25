package org.spsochnev.vacancies.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.spsochnev.vacancies.config.StatisticsRequestMessage;
import org.spsochnev.vacancies.config.UserEligibilityMessage;
import org.spsochnev.vacancies.payment.User;
import org.spsochnev.vacancies.payment.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsRequestService.class);

    @Autowired
    private StatisticsRequestRepository statisticsRequestRepository;

    @Autowired
    private StatisticsEndpointService statisticsEndpointService;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, UserEligibilityMessage> kafkaTemplate;

    @Value(value = "${vacancy.kafka.topic.eligibility}")
    private String userEligibilityTopicName;

    public List<StatisticsRequestDTO> getStatisticsRequestHistoryByUsername(String username) {
        User user = userService.getUserByUsernameOrThrow(username);
        return statisticsRequestRepository.findAllByUser(user).stream().
                map(StatisticsRequestDTO::new).
                collect(Collectors.toList());
    }

    @KafkaListener(topics = "${vacancy.kafka.topic.statistics.request}")
    @Transactional(propagation = Propagation.REQUIRED)
    public void listen(@Payload StatisticsRequestMessage message) {
        LOG.info("Received {}", message);
        Optional<User> o = userService.getUserByUsername(message.getUsername());
        if (o.isEmpty()) {
            LOG.error("Username " + message.getUsername() + " was not found. Statistics request message was ignored.");
            return;
        }
        User user = o.get();
        StatisticsEndpoint endpoint = statisticsEndpointService.getStatisticsEndpointByUrl(message.getEndpoint());
        BigDecimal price = endpoint.getPrice();
        StatisticsRequest request = new StatisticsRequest(user, endpoint, LocalDateTime.now(), price);
        userService.chargeThePrice(user, price);
        statisticsRequestRepository.save(request);
        if (user.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            UserEligibilityMessage eligibilityMessage = new UserEligibilityMessage(user.getUsername(), false);
            ListenableFuture<SendResult<String, UserEligibilityMessage>> future =
                    kafkaTemplate.send(userEligibilityTopicName, eligibilityMessage);

            future.addCallback(new ListenableFutureCallback<>() {

                @Override
                public void onSuccess(SendResult<String, UserEligibilityMessage> result) {
                    LOG.info("Sent {} with offset=[{}]", eligibilityMessage, result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOG.error("Unable to send message due to: {}", ex.getMessage());
                }
            });
        }
    }

}
