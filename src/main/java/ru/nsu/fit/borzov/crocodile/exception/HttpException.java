package ru.nsu.fit.borzov.crocodile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends Exception {
    private final HttpStatus status;
    private final ExceptionMessage clientMessage;

    public HttpException(HttpStatus status, ExceptionMessage clientMessage) {
        this.status = status;
        this.clientMessage = clientMessage;
    }

    public HttpException(HttpStatus status, ExceptionMessage clientMessage, String message) {
        super(message);
        this.status = status;
        this.clientMessage = clientMessage;
    }
}
