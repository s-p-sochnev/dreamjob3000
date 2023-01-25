package org.spsochnev.vacancies.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.spsochnev.vacancies.payment.User;
import org.spsochnev.vacancies.payment.UserRepository;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> o = userRepository.findByUsername(username);
        return o.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
