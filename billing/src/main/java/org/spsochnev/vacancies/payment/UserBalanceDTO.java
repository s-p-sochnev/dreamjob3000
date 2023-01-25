package org.spsochnev.vacancies.payment;

import java.math.BigDecimal;

public record UserBalanceDTO (String username, BigDecimal balance) {

    public UserBalanceDTO(User user) {
        this(user.getUsername(), user.getBalance());
    }
}
