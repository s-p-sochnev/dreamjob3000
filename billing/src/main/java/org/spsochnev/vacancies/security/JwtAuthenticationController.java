package org.spsochnev.vacancies.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtUserDetailsService jwtUserDetailsService;

    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationController(
                                       @Autowired AuthenticationManager authenticationManager,
                                       @Autowired JwtUserDetailsService jwtUserDetailsService,
                                       @Autowired JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@ModelAttribute JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
