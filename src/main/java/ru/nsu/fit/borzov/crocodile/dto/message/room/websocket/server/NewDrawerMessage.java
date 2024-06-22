package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class NewDrawerMessage extends ServerMessage {
    private final long userId;
    private final String userName;

    public NewDrawerMessage(long userId, String userName) {
        super(ServerMessageType.NEW_DRAWER_MESSAGE);
        this.userId = userId;
        this.userName = userName;
    }
}
