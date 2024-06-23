package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChooseWordRequest {
    private @NotNull String index;
}
