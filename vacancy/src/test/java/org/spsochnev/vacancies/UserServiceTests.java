package org.spsochnev.vacancies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spsochnev.vacancies.collector.User;
import org.spsochnev.vacancies.collector.UserRepository;
import org.spsochnev.vacancies.collector.UserService;
import org.spsochnev.vacancies.config.UserEligibilityMessage;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("User service updated DB when receiving user eligibility message")
    public void testUserEligibilityMessageReceived() {
        UserEligibilityMessage message = new UserEligibilityMessage("1", false);
        User user = new User(1, "1", "1", true);
        when(userRepository.findByUsername("1")).thenReturn(Optional.of(user));

        userService.listen(message);

        user = new User(1, "1", "1", false);
        verify(userRepository).save(user);
        verify(userRepository).save(any());
    }

}
