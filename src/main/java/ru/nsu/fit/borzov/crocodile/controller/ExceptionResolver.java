package ru.nsu.fit.borzov.crocodile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nsu.fit.borzov.crocodile.exception.ExceptionMessage;
import ru.nsu.fit.borzov.crocodile.exception.HttpException;
import ru.nsu.fit.borzov.crocodile.exception.InternalServerErrorException;

@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalError() {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpException.class})
    public ResponseEntity<ExceptionMessage> invalidAuthDataError(HttpException exception) {
        return new ResponseEntity<>(exception.getClientMessage(), exception.getStatus());
    }
}