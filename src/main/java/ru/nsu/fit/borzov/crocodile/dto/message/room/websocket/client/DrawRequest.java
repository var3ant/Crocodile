package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

@Data
public class DrawRequest {
    private Point startPoint;
    private Point finishPoint;
    private int size;
    private String color;
}
