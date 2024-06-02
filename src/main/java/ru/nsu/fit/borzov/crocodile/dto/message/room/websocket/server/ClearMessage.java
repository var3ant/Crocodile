package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class ClearMessage extends ServerMessage {
    public ClearMessage() {
        super(ServerMessageType.CLEAR_MESSAGE);
    }
}
