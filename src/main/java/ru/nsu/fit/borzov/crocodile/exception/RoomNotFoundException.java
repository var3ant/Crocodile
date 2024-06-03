package ru.nsu.fit.borzov.crocodile.exception;

import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends HttpException {
    public RoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, ExceptionMessage.ROOM_NOT_FOUND_MESSAGE);
    }
}
