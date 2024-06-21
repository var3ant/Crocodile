package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class IllegalUserException extends HttpException {
    public IllegalUserException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.ILLEGAL_USER_MESSAGE);
    }
}
