package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends HttpException {
    public AuthenticationException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.BAD_AUTHENTICATOR_MESSAGE);
    }
}
