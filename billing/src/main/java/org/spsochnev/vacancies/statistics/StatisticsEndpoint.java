package org.spsochnev.vacancies.statistics;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="statistics_endpoint")
public class StatisticsEndpoint {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public StatisticsEndpoint() {}

    public StatisticsEndpoint(String endpoint, BigDecimal price) {
        this.endpoint = endpoint;
        this.price = price;
    }

    public StatisticsEndpoint(Integer id, String endpoint, BigDecimal price) {
        this.id = id;
        this.endpoint = endpoint;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getEndpoint() {
        return endpoint;
    }

}