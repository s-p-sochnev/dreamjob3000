package org.spsochnev.vacancies.payment;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("Wrong username/password!", headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("User not found!", headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public String handleRuntimeException(Exception e, WebRequest request) {
        return "redirect:/error";
    }

}
