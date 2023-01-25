package org.spsochnev.vacancies.config;

import java.io.Serial;
import java.io.Serializable;

public class UserEligibilityMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 2882476800900L;

    private String username;

    private Boolean eligible;

    public UserEligibilityMessage() {
    }

    public UserEligibilityMessage(String username, Boolean eligible) {
        this.username = username;
        this.eligible = eligible;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEligibility() {
        return eligible;
    }

    public void setEligibility(Boolean eligible) {
        this.eligible = eligible;
    }

    @Override
    public String toString() {
        return "UserEligibilityMessage{" +
                "username='" + username + '\'' +
                ", eligible=" + eligible +
                '}';
    }

}
