package org.spsochnev.vacancies.statistics;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatisticsEndpointRepository extends CrudRepository<StatisticsEndpoint, Integer> {

    Optional<StatisticsEndpoint> findByEndpoint(String endpoint);

}
