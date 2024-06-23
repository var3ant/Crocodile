package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends HttpException {
    public UserNotFoundException() {
        super(HttpStatus.BAD_REQUEST, ExceptionMessage.USER_NOT_FOUND_MESSAGE);
    }
}
