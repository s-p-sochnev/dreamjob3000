package org.spsochnev.vacancies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spsochnev.vacancies.config.StatisticsRequestMessage;
import org.spsochnev.vacancies.payment.User;
import org.spsochnev.vacancies.payment.UserService;
import org.spsochnev.vacancies.statistics.StatisticsEndpoint;
import org.spsochnev.vacancies.statistics.StatisticsEndpointService;
import org.spsochnev.vacancies.statistics.StatisticsRequestRepository;
import org.spsochnev.vacancies.statistics.StatisticsRequestService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsRequestServiceTests {

    @Mock
    private StatisticsRequestRepository statisticsRequestRepository;

    @Mock
    private StatisticsEndpointService statisticsEndpointService;

    @Mock
    private UserService userService;

    @InjectMocks
    private StatisticsRequestService statisticsRequestService;

    @Test
    @DisplayName("Statistics request service charges user for request")
    public void testStatisticsRequestMessageReceived() {
        StatisticsRequestMessage message = new StatisticsRequestMessage("1", "/statistics/salary");
        User user = new User(1, "1", "1", BigDecimal.valueOf(20));
        when(userService.getUserByUsername("1")).thenReturn(Optional.of(user));
        StatisticsEndpoint endpoint = new StatisticsEndpoint(1, "/statistics/salary", BigDecimal.valueOf(10));
        when(statisticsEndpointService.getStatisticsEndpointByUrl("/statistics/salary")).thenReturn(endpoint);


        statisticsRequestService.listen(message);

        verify(userService).chargeThePrice(user, BigDecimal.valueOf(10));
        verify(userService).chargeThePrice(any(), any());
        verify(statisticsRequestRepository).save(any());
    }

}
