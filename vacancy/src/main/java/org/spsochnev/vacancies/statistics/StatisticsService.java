package org.spsochnev.vacancies.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.spsochnev.vacancies.collector.Skill;
import org.spsochnev.vacancies.collector.SkillRepository;
import org.spsochnev.vacancies.collector.UserService;
import org.spsochnev.vacancies.collector.VacancyRepository;
import org.spsochnev.vacancies.config.StatisticsRequestMessage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, StatisticsRequestMessage> kafkaTemplate;

    @Value(value = "${vacancy.kafka.topic.statistics.request}")
    private String statisticsRequestTopicName;

    private void verifyUserEligibility(String username) {
        if (!userService.isUserEligible(username)) {
            throw new InsufficientFundsException();
        }
    }

    private void sendStatisticsRequestMessage(String username, String statisticsEndpoint) {
        StatisticsRequestMessage message = new StatisticsRequestMessage(username, statisticsEndpoint);
        ListenableFuture<SendResult<String, StatisticsRequestMessage>> future =
                kafkaTemplate.send(statisticsRequestTopicName, message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, StatisticsRequestMessage> result) {
                LOG.info("Sent {} with offset=[{}]", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.error("Unable to send message due to: {}", ex.getMessage());
            }
        });
    }

    public List<ExperienceStatDTO> getAverageSalaryByExp() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        verifyUserEligibility(username);
        List<ExperienceStatDTO> res = vacancyRepository.getAverageSalaryByExperience();
        sendStatisticsRequestMessage(username, "/statistics/salary");
        return res;
    }

    public List<String> getTop10SkillsByExp(String exp) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        verifyUserEligibility(username);
        List<Skill> s = skillRepository.findTop10SkillsByExperience(exp);
        sendStatisticsRequestMessage(username, "/statistics/skills");
        return s.stream().
                map(Skill::getDescription).
                collect(Collectors.toList());
    }
}
