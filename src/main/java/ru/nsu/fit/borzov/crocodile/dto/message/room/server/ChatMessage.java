package ru.nsu.fit.borzov.crocodile.dto.message.room.server;

import lombok.Getter;

@Getter
public class ChatMessage extends ServerMessage {
    private final String text;
    private final Long userId;

    public ChatMessage(String text, Long userId) {
        super(ServerMessageType.CHAT_MESSAGE);
        this.text = text;
        this.userId = userId;
    }
}
