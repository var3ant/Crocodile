package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class InfoMessage extends ServerMessage {
    private final String text;

    public InfoMessage(String text) {
        super(ServerMessageType.INFO_MESSAGE);
        this.text = text;
    }
}
