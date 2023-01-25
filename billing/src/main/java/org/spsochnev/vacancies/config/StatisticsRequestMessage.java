package org.spsochnev.vacancies.config;

import java.io.Serial;
import java.io.Serializable;

public class StatisticsRequestMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 2882747391300L;

    private String username;

    private String endpoint;

    public StatisticsRequestMessage() { }

    public StatisticsRequestMessage(String username, String endpoint) {
        this.username = username;
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "StatisticsRequestMessage{" +
                "username='" + username + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }

}
