package org.spsochnev.vacancies.statistics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record StatisticsRequestDTO(String username, LocalDateTime requestTime, String endpoint, BigDecimal price) {

    public StatisticsRequestDTO(StatisticsRequest request) {
        this(request.getUser().getUsername(),
                request.getRequestTime(),
                request.getEndpoint().getEndpoint(),
                request.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsRequestDTO that = (StatisticsRequestDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(requestTime, that.requestTime)
                && Objects.equals(endpoint, that.endpoint)
                && price != null && that.price != null && price.compareTo(that.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, requestTime, endpoint, price);
    }
}
