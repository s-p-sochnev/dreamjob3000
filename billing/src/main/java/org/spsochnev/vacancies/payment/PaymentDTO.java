package org.spsochnev.vacancies.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record PaymentDTO(String username, LocalDateTime paymentTime, BigDecimal amount) {

    public PaymentDTO(Payment payment) {
        this(payment.getUser().getUsername(), payment.getPaymentTime(), payment.getAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(paymentTime, that.paymentTime)
                && amount != null && that.amount != null && amount.compareTo(that.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, paymentTime, amount);
    }
}
