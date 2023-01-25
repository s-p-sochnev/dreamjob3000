package org.spsochnev.vacancies.collector;

import javax.persistence.*;

@Entity
@Table(name="experience")
public class Experience {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="description", nullable = false, unique = true)
    private String description;

    public Experience() {}

    public Experience(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
