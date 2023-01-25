package org.spsochnev.vacancies.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.spsochnev.vacancies.collector.User;
import org.spsochnev.vacancies.collector.UserRepository;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> o = userRepository.findByUsername(username);
        return o.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
