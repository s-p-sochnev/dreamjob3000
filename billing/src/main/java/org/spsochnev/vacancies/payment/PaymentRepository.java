package org.spsochnev.vacancies.payment;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    List<Payment> findAllByUser(User user);

}
