package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class NewDrawerMessage extends ServerMessage {
    private final long userId;

    public NewDrawerMessage(long userId) {
        super(ServerMessageType.NEW_DRAWER_MESSAGE);
        this.userId = userId;
    }
}
