package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class IlligalRequestArgumentException extends HttpException {
    public IlligalRequestArgumentException(String message) {
        super(HttpStatus.BAD_REQUEST, ExceptionMessage.ILLEGAL_ARGUMENT_EXCEPTION,  message);
    }
}
