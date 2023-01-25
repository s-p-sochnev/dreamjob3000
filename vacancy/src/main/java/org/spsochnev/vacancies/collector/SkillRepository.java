package org.spsochnev.vacancies.collector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends CrudRepository<Skill, Integer> {

    Optional<Skill> findByDescription(String description);

    @Query(value = """
        SELECT S.ID, S.DESCRIPTION FROM SKILL AS S
        JOIN VACANCY_SKILL_XREF AS X ON S.ID=X.SKILL_ID
        JOIN VACANCY AS V ON X.VACANCY_ID=V.ID
        WHERE V.EXPERIENCE_ID IN
        (SELECT ID FROM EXPERIENCE AS E WHERE E.DESCRIPTION=?1)
        GROUP BY S.ID
        ORDER BY COUNT(S.ID) DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Skill> findTop10SkillsByExperience(String exp);

}
