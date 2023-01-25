package org.spsochnev.vacancies.statistics;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message, Throwable ex) { super(message, ex); }

    public InsufficientFundsException(String message) { super(message); }

    public InsufficientFundsException() { super(); }

}
