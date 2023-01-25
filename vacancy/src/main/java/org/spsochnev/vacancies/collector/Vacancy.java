package org.spsochnev.vacancies.collector;

import org.springframework.data.domain.Persistable;
import org.spsochnev.vacancies.statistics.ExperienceStatDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedNativeQuery(name = "Vacancy.getAverageSalaryByExperience",
        query = """
        SELECT E.DESCRIPTION AS DESCRIPTION,
        ROUND(
            AVG(
                (
                    COALESCE(V.SALARY_TO, V.SALARY_FROM) + COALESCE(V.SALARY_FROM, V.SALARY_TO)
                ) / 2
                * COALESCE(C.EXCHANGE_RATE, 1)
            )
        ) AS AVERAGE_SALARY,
        (SELECT COUNT(V2.ID)
            FROM VACANCY AS V2
            RIGHT JOIN EXPERIENCE AS E2 ON V2.EXPERIENCE_ID=E2.ID
            WHERE E2.ID=E.ID
        ) AS VACANCY_COUNT
        FROM VACANCY AS V JOIN CURRENCY AS C ON V.CURRENCY_ID=C.ID
        RIGHT JOIN EXPERIENCE AS E ON V.EXPERIENCE_ID=E.ID
        GROUP BY DESCRIPTION
    """,
        resultSetMapping = "Mapping.ExperienceStatDTO")
@SqlResultSetMapping(name = "Mapping.ExperienceStatDTO",
        classes = @ConstructorResult(targetClass = ExperienceStatDTO.class,
                columns = {@ColumnResult(name = "DESCRIPTION"),
                        @ColumnResult(name = "AVERAGE_SALARY"),
                        @ColumnResult(name = "VACANCY_COUNT")}))
@Entity
@Table(name="vacancy")
public class Vacancy implements Persistable<Integer> {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="salary_from")
    private Integer salaryFrom;

    @Column(name="salary_to")
    private Integer salaryTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "vacancy_skill_xref",
            joinColumns = {@JoinColumn(name = "vacancy_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> keySkills = new HashSet<>();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Integer salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Integer getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Integer salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public Set<Skill> getKeySkills() {
        return keySkills;
    }

    public void setKeySkills(Set<Skill> keySkills) {
        this.keySkills = keySkills;
    }

}
