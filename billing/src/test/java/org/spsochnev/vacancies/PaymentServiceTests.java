package org.spsochnev.vacancies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.spsochnev.vacancies.config.UserEligibilityMessage;
import org.spsochnev.vacancies.payment.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserService userService;

    @Mock
    private KafkaTemplate<String, UserEligibilityMessage> kafkaTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Payment service sends user eligibility message")
    public void testUserEligibilityMessageSent() {
        User user = new User(1, "1", "1", BigDecimal.valueOf(10));
        when(userService.getUserByUsernameOrThrow("1")).thenReturn(user);
        when(kafkaTemplate.send(any(), any())).thenReturn(mock(ListenableFuture.class));

        UserBalanceDTO balanceDTO = paymentService.processUserPayment("1", BigDecimal.ZERO);

        verify(userService).topUpUserBalance(user, BigDecimal.ZERO);
        verify(userService).topUpUserBalance(any(), any());
        verify(paymentRepository).save(any());
        verify(kafkaTemplate).send(any(), any());
        assertEquals(user.getUsername(), balanceDTO.username());
        assertEquals(0, balanceDTO.balance().compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("Payment service doesn't send user eligibility message on negative balance")
    public void testUserEligibilityMessageNotSent() {
        User user = new User(1, "1", "1", BigDecimal.valueOf(-10));
        when(userService.getUserByUsernameOrThrow("1")).thenReturn(user);

        UserBalanceDTO balanceDTO = paymentService.processUserPayment("1", BigDecimal.ZERO);

        verify(userService).topUpUserBalance(user, BigDecimal.ZERO);
        verify(userService).topUpUserBalance(any(), any());
        verify(paymentRepository).save(any());
        verify(kafkaTemplate, times(0)).send(any(), any());
        assertEquals(user.getUsername(), balanceDTO.username());
        assertEquals(0, balanceDTO.balance().compareTo(BigDecimal.valueOf(-10)));
    }

}
