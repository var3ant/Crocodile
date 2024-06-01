package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class ChatMessage extends ServerMessage {
    private final String text;
    private final long userId;

    public ChatMessage(String text, long userId) {
        super(ServerMessageType.CHAT_MESSAGE);
        this.text = text;
        this.userId = userId;
    }
}
