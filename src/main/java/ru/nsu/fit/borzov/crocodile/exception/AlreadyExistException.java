package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends HttpException {
    public AlreadyExistException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.ALREADY_EXIST_MESSAGE);
    }
}
