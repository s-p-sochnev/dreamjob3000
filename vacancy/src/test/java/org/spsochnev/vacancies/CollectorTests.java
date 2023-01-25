package org.spsochnev.vacancies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.spsochnev.vacancies.collector.Collector;
import org.spsochnev.vacancies.collector.CurrencyService;
import org.spsochnev.vacancies.collector.InfoRepository;
import org.spsochnev.vacancies.collector.VacancyService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectorTests {

    @Mock
    private InfoRepository infoRepository;

    @Mock
    private VacancyService vacancyService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private Collector collector;

    @Test
    @DisplayName("Data collection is skipped if last update date is fresh")
    public void testLastUpdateDateFresh() {
        ReflectionTestUtils.setField(collector, "updatePeriodDays", 7);
        when(infoRepository.getLastUpdateDate()).thenReturn(LocalDate.now());

        collector.collect();

        verify(vacancyService, times(0)).updateVacancies();
        verify(infoRepository, times(0)).updateLastUpdateDate();
        verify(currencyService, times(0)).updateCurrencies();
    }

    @Test
    @DisplayName("Data collection is performed if last update date is obsolete")
    public void testLastUpdateDateObsolete() {
        ReflectionTestUtils.setField(collector, "updatePeriodDays", 7);
        when(infoRepository.getLastUpdateDate()).thenReturn(LocalDate.now().minusDays(7));

        collector.collect();

        verify(vacancyService).updateVacancies();
        verify(infoRepository).updateLastUpdateDate();
        verify(currencyService).updateCurrencies();
    }

}
