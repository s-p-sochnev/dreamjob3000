package org.spsochnev.vacancies.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class StatisticsEndpointService {

    @Autowired
    private StatisticsEndpointRepository statisticsEndpointRepository;

    public StatisticsEndpoint getStatisticsEndpointByUrl(String url) {
        Optional<StatisticsEndpoint> o = statisticsEndpointRepository.findByEndpoint(url);
        return o.orElseThrow(() -> new EntityNotFoundException("Endpoint '" + url + "' was not found"));
    }
}
