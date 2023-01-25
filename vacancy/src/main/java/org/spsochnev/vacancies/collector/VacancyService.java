package org.spsochnev.vacancies.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VacancyService {

    private static Map<String, Skill> skillCache = new HashMap<>();

    private static Map<String, Experience> experienceCache = new HashMap<>();


    @Autowired
    private VacancySource vacancySource;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExperienceRepository experienceRepository;

    public void updateVacancies() {
        List<Vacancy> vacancies = vacancySource.getVacancies();
        writeVacanciesToDB(vacancies);
    }

    private Vacancy save(Vacancy vacancy) {
        saveVacancySkills(vacancy);
        updateVacancySkillsWithIDs(vacancy);

        saveVacancyExperience(vacancy);
        updateVacancyExperienceWithID(vacancy);

        updateVacancyCurrencyWithID(vacancy);

        if (!vacancyRepository.existsById(vacancy.getId())) vacancyRepository.save(vacancy);
        return vacancy;
    }

    private void saveVacancySkills(Vacancy vacancy) {
        vacancy.getKeySkills().stream().
                filter(skill -> !skillCache.containsKey(skill.getDescription())).
                forEach(skill -> {
                    String description = skill.getDescription();
                    Optional<Skill> skillOptional = skillRepository.findByDescription(description);
                    Skill skillWithID = skillOptional.orElseGet(() -> skillRepository.save(skill));
                    skillCache.put(description, skillWithID);
                });
    }

    private void updateVacancySkillsWithIDs(Vacancy vacancy) {
        vacancy.setKeySkills(vacancy.getKeySkills().stream().
                map(skill -> skillCache.get(skill.getDescription())).
                collect(Collectors.toSet()));
    }

    private void saveVacancyExperience(Vacancy vacancy) {
        Experience experience = vacancy.getExperience();
        if (experience == null) return;

        String description = experience.getDescription();
        if (experienceCache.containsKey(description)) return;

        Optional<Experience> o = experienceRepository.findByDescription(description);
        Experience experienceWithID = o.orElseGet(() -> experienceRepository.save(experience));
        experienceCache.put(description, experienceWithID);
    }

    private void updateVacancyExperienceWithID(Vacancy vacancy) {
        Experience experience = vacancy.getExperience();
        if (experience == null) return;

        vacancy.setExperience(experienceCache.get(experience.getDescription()));
    }

    private void updateVacancyCurrencyWithID(Vacancy vacancy) {
        Currency currency = vacancy.getCurrency();
        if (currency == null) return;

        vacancy.setCurrency(currencyService.getCurrencyByCode(currency.getCode()));
    }

    private void writeVacanciesToDB(List<Vacancy> vacancies) {
        vacancies.forEach(vac -> save(vac));
    }

}
