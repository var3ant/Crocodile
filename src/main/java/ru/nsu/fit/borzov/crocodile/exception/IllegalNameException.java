package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class IllegalNameException extends HttpException {
    public IllegalNameException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.ILLEGAL_NAME_MESSAGE);
    }
}
