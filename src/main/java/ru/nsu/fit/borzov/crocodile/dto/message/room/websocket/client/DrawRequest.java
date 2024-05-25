package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

@Data
public class DrawRequest {
    private Point startPoint;
    private Point finishPoint;

    public DrawRequest() {
    }

    public DrawRequest(Point startPoint, Point finishPoint) {
        this.startPoint = startPoint;
        this.finishPoint = finishPoint;
    }
}
