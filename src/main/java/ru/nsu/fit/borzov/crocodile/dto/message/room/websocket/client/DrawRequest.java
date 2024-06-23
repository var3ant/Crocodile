package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DrawRequest {
    private @NotNull Point startPoint;
    private @NotNull Point finishPoint;
    private int size;
    private @NotNull String color;
}
