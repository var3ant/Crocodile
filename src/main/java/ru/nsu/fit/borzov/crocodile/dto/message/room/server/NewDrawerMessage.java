package ru.nsu.fit.borzov.crocodile.dto.message.room.server;

import lombok.Getter;

@Getter
public class NewDrawerMessage extends ServerMessage {
    private final Long userId;

    public NewDrawerMessage(Long userId) {
        super(ServerMessageType.NEW_DRAWER_MESSAGE);
        this.userId = userId;
    }
}
