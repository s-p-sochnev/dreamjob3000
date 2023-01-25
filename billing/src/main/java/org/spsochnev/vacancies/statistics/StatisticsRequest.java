package org.spsochnev.vacancies.statistics;

import org.spsochnev.vacancies.payment.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="statistics_request")
public class StatisticsRequest {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private StatisticsEndpoint endpoint;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public StatisticsRequest() {}

    public StatisticsRequest(User user, StatisticsEndpoint endpoint, BigDecimal price) {
        this.user = user;
        this.endpoint = endpoint;
        this.price = price;
    }

    public StatisticsRequest(User user, StatisticsEndpoint endpoint, LocalDateTime requestTime, BigDecimal price) {
        this.user = user;
        this.endpoint = endpoint;
        this.requestTime = requestTime;
        this.price = price;
    }

    public StatisticsRequest(Integer id, User user, StatisticsEndpoint endpoint, LocalDateTime requestTime, BigDecimal price) {
        this.id = id;
        this.user = user;
        this.endpoint = endpoint;
        this.requestTime = requestTime;
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public StatisticsEndpoint getEndpoint() {
        return endpoint;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
