package org.spsochnev.vacancies.payment;

import java.math.BigDecimal;

public record PaymentRequestDTO(BigDecimal amount) {
}
