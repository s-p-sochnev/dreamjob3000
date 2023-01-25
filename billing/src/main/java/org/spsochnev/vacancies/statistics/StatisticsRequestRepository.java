package org.spsochnev.vacancies.statistics;

import org.springframework.data.repository.CrudRepository;
import org.spsochnev.vacancies.payment.User;

import java.util.List;

public interface StatisticsRequestRepository extends CrudRepository<StatisticsRequest, Integer> {

    List<StatisticsRequest> findAllByUser(User user);

}
