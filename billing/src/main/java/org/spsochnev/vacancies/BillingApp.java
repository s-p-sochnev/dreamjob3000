package org.spsochnev.vacancies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingApp {

    private static final Logger LOG = LoggerFactory.getLogger(BillingApp.class);

    public static void main( String[] args )
    {
        LOG.info("Running Billing App");
        SpringApplication.run(BillingApp.class, args);
    }
}
