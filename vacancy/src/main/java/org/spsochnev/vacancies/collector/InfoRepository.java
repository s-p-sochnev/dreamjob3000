package org.spsochnev.vacancies.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class InfoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(InfoRepository.class);

    @Autowired
    private JdbcTemplate template;

    public LocalDate getLastUpdateDate() {
        Date result = template.queryForObject(
                "SELECT last_update_date FROM info LIMIT 1", Date.class);
        if (result == null) {
            LOG.error("Last update date query returned NULL");
            return LocalDate.now().minusDays(8);
        };
        return result.toLocalDate();
    }

    public void updateLastUpdateDate() {
        template.update("UPDATE info SET last_update_date=CURRENT_DATE()");
    }

}
