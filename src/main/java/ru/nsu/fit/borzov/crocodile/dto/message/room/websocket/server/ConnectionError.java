package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class ConnectionError extends ServerMessage {
    private final long roomId;

    public ConnectionError(long roomId) {
        super(ServerMessageType.CONNECTION_ERROR_MESSAGE);
        this.roomId = roomId;
    }
}