package org.spsochnev.vacancies.collector;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    private Integer id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String hashedPassword;

    @NotNull
    private Boolean eligible;

    public User() {}

    public User(String username, String hashedPassword, Boolean eligible) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.eligible = eligible;
    }

    public User(Integer id, String username, String hashedPassword, Boolean eligible) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.eligible = eligible;
    }

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

    public Boolean getEligibility() { return eligible; }

    public void setEligibility(Boolean eligible) { this.eligible = eligible; }

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
                && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(eligible, user.eligible);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, hashedPassword, eligible);
    }

}
