package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.ReactionType;

import javax.validation.constraints.NotNull;

@Data
public class ReactionRequest {
    private @NotNull String messageId;
    private @NotNull ReactionType reaction;
}
