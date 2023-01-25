package org.spsochnev.vacancies.collector;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="code", nullable = false, unique = true)
    private String code;

    @Column(name="exchange_rate", nullable = false)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    public Currency() {}

    public Currency(String code) {
        this.code = code;
    }

    public Currency(String code, BigDecimal rate) {
        this.code = code;
        this.exchangeRate = rate;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return code + ": " + exchangeRate;
    }

}
