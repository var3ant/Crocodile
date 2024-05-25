package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ServerMessage {
    private final ServerMessageType type;
    private final UUID id;

    protected ServerMessage(ServerMessageType type) {
        this.type = type;
        this.id = UUID.randomUUID();
    }
}
