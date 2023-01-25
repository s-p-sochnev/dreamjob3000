package org.spsochnev.vacancies.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class Collector {

    private static final Logger LOG = LoggerFactory.getLogger(Collector.class);

    @Value("${vacancy.update.period.days}")
    private Integer updatePeriodDays;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private InfoRepository infoRepository;

    @Scheduled(fixedRateString = "${vacancy.update.period.days}", timeUnit = TimeUnit.DAYS)
    public void collect() {
        LocalDate date = infoRepository.getLastUpdateDate();
        LOG.info("Last update date in DB: " + date.toString());

        if (date.isAfter(LocalDate.now().minusDays(updatePeriodDays))) {
            LOG.info("Data is not obsolete yet, collecting skipped");
            return;
        }

        LOG.info("Collecting started");

        vacancyService.updateVacancies();

        infoRepository.updateLastUpdateDate();

        currencyService.updateCurrencies();

    }


}
