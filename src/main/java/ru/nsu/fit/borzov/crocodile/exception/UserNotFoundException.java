package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends HttpException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND_MESSAGE);
    }
}
