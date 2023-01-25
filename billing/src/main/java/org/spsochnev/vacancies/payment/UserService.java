package org.spsochnev.vacancies.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByUsernameOrThrow(String username) {
        Optional<User> o = getUserByUsername(username);
        return o.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserBalanceDTO getUserBalanceByUsername(String username) {
        User user = getUserByUsernameOrThrow(username);
        return new UserBalanceDTO(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void topUpUserBalance(User user, BigDecimal amount) {
        user.topUpBalance(amount);
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void chargeThePrice(User user, BigDecimal price) {
        user.chargeThePrice(price);
        userRepository.save(user);
    }

}
