package org.spsochnev.vacancies.payment;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payment")
public class Payment {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public Payment() {}

    public Payment(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public Payment(User user, LocalDateTime paymentTime, BigDecimal amount) {
        this.user = user;
        this.paymentTime = paymentTime;
        this.amount = amount;
    }

    public Payment(Integer id, User user, LocalDateTime paymentTime, BigDecimal amount) {
        this.id = id;
        this.user = user;
        this.paymentTime = paymentTime;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
