package org.spsochnev.vacancies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VacancyApp {

    private static final Logger LOG = LoggerFactory.getLogger(VacancyApp.class);

    public static void main( String[] args ) {
        LOG.info("Running Vacancy App");
        SpringApplication.run(VacancyApp.class, args);
    }
}
