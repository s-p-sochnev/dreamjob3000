package org.spsochnev.vacancies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.concurrent.ListenableFuture;
import org.spsochnev.vacancies.collector.Skill;
import org.spsochnev.vacancies.collector.SkillRepository;
import org.spsochnev.vacancies.collector.UserService;
import org.spsochnev.vacancies.config.StatisticsRequestMessage;
import org.spsochnev.vacancies.statistics.InsufficientFundsException;
import org.spsochnev.vacancies.statistics.StatisticsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTests {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserService userService;

    @Mock
    private KafkaTemplate<String, StatisticsRequestMessage> kafkaTemplate;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    @DisplayName("Statistic service returns descriptions of top skills")
    public void testTopSkillDescriptions() {
        List<Skill> skills = List.of(
                new Skill(1, "Java"),
                new Skill(2, "OOP"),
                new Skill(3, "Spring"),
                new Skill(4, "Docker"),
                new Skill(5, "Hibernate ORM"),
                new Skill(6, "Kafka"),
                new Skill(7, "SQL"),
                new Skill(8, "REST API"),
                new Skill(9, "Apache Maven"),
                new Skill(10, "Git")
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.isUserEligible(any())).thenReturn(true);
        when(skillRepository.findTop10SkillsByExperience(anyString())).thenReturn(skills);
        when(kafkaTemplate.send(any(), any())).thenReturn(mock(ListenableFuture.class));

        List<String> res = statisticsService.getTop10SkillsByExp("");

        assertEquals(skills.size(), res.size());
        skills.forEach(skill -> assertTrue(res.contains(skill.getDescription())));
        verify(kafkaTemplate).send(any(), any());
    }

    @Test
    @DisplayName("Statistic service throws InsufficientFundsException when user is not eligible")
    public void testInsufficientFundsExceptionIsThrown() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.isUserEligible(any())).thenReturn(false);

        assertThrows(InsufficientFundsException.class, () -> statisticsService.getTop10SkillsByExp(""));
        verify(kafkaTemplate, times(0)).send(any(), any());
    }

}
