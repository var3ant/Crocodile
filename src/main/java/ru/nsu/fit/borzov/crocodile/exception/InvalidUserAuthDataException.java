package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserAuthDataException extends HttpException {
    public InvalidUserAuthDataException() {
        super(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_USER_AUTH_DATA_MESSAGE);
    }
}
