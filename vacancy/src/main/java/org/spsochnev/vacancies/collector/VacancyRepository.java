package org.spsochnev.vacancies.collector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.spsochnev.vacancies.statistics.ExperienceStatDTO;

import java.util.List;

public interface VacancyRepository extends CrudRepository<Vacancy, Integer> {

   @Query(nativeQuery = true)
   List<ExperienceStatDTO> getAverageSalaryByExperience();

}
