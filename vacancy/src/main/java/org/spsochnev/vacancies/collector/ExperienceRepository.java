package org.spsochnev.vacancies.collector;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExperienceRepository extends CrudRepository<Experience, Integer> {

    Optional<Experience> findByDescription(String description);

}
