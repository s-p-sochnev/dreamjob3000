package org.spsochnev.vacancies.payment;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public User() {}

    public User(String username, String hashedPassword, BigDecimal balance) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.balance = balance;
    }

    public User(Integer id, String username, String hashedPassword, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.balance = balance;
    }

    public void topUpBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public void chargeThePrice(BigDecimal price) { this.balance = balance.subtract(price).max(BigDecimal.ZERO); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public BigDecimal getBalance() { return balance; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username)
                && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, hashedPassword, balance);
    }
}
