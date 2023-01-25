package org.spsochnev.vacancies.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.spsochnev.vacancies.config.UserEligibilityMessage;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public Optional<User> loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User loadUserByUsernameOrThrow(String username) throws UsernameNotFoundException {
        Optional<User> o = loadUserByUsername(username);
        return o.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean isUserEligible(String username) {
        User user = loadUserByUsernameOrThrow(username);
        return user.getEligibility();
    }

    @KafkaListener(topics = "${vacancy.kafka.topic.eligibility}")
    public void listen(@Payload UserEligibilityMessage message) {
        LOG.info("Received {}", message);
        Optional<User> o = loadUserByUsername(message.getUsername());
        if (o.isEmpty()) {
            LOG.error("Username " + message.getUsername() + " was not found. User eligibility message was ignored.");
            return;
        }
        User user = o.get();
        user.setEligibility(message.getEligibility());
        userRepository.save(user);
    }
}
