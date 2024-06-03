package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class WrongGameRoleException extends HttpException {
    public WrongGameRoleException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.WRONG_GAME_ROLE_EXCEPTION);
    }
}
