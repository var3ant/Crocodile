package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DrawRequest {
    private @NotNull Point startPoint;
    private @NotNull Point finishPoint;
    private int size;
    private @NotNull String color;
}
