package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class UserNotInRoomException extends HttpException {
    public UserNotInRoomException() {
        super(HttpStatus.CONFLICT, ExceptionMessage.USER_NOT_IN_ROOM_MESSAGE);
    }
}
