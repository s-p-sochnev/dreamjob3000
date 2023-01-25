package org.spsochnev.vacancies.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ExperienceStatDTO (String description, BigDecimal averageSalary, BigInteger vacancyCount) {

}
