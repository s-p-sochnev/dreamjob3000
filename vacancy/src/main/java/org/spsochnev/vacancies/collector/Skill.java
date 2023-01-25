package org.spsochnev.vacancies.collector;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="skill")
public class Skill {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="description", nullable = false, unique = true)
    private String description;

    public Skill(String description) {
        this.description = description;
    }

    public Skill(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Skill() {}

    public String getDescription() {
        return description;
    }

}
