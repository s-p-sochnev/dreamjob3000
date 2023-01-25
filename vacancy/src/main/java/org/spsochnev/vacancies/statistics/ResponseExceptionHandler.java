package org.spsochnev.vacancies.statistics;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>("Insufficient funds. Please top-up your balance.", headers,
                HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(Exception.class)
    public String handleRuntimeException(Exception e, WebRequest request) {
        return "redirect:/error";
    }

}
