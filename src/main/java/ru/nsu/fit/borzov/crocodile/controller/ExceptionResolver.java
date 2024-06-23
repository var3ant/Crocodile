package ru.nsu.fit.borzov.crocodile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nsu.fit.borzov.crocodile.exception.ExceptionMessage;
import ru.nsu.fit.borzov.crocodile.exception.HttpException;
import ru.nsu.fit.borzov.crocodile.exception.InternalServerErrorException;

@ControllerAdvice
public class ExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleError(InternalServerErrorException error) {
        logger.error("Server error:", error);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleRuntimeError(RuntimeException runtimeException) {
        logger.error("Runtime exception:", runtimeException);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpException.class})
    public ResponseEntity<ExceptionMessage> handleException(HttpException exception) {
        logger.info("User exception:", exception);
        return new ResponseEntity<>(exception.getClientMessage(), exception.getStatus());
    }
}