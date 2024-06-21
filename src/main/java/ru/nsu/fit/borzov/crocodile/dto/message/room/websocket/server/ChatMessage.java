package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChatMessage extends ServerMessage {
    private final long userId;
    private final UUID messageId;
    private final String userName;
    private final String text;
    private final ReactionType reaction;

    public ChatMessage(long userId, String userName, String text, ReactionType reaction) {
        super(ServerMessageType.CHAT_MESSAGE);
        messageId = UUID.randomUUID();
        this.text = text;
        this.userName = userName;
        this.userId = userId;
        this.reaction = reaction;
    }

    public ChatMessage(long userId, String userName, String text) {
        this(userId, userName, text, ReactionType.NOT_SELECTED);
    }
}
