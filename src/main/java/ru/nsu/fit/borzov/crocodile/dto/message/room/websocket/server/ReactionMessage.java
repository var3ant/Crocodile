package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class ReactionMessage extends ServerMessage {
    private final String messageId;
    private final ReactionType reaction;

    public ReactionMessage(String messageId, ReactionType reaction) {
        super(ServerMessageType.REACTION_MESSAGE);
        this.messageId = messageId;
        this.reaction = reaction;
    }
}