package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.ReactionType;

@Data
public class ReactionRequest {
    private @NotNull String messageId;
    private @NotNull ReactionType reaction;
}
