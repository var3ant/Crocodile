package ru.nsu.fit.borzov.crocodile.dto.message.room.server;

import lombok.AccessLevel;
import lombok.Getter;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.DrawRequest;
import lombok.experimental.Delegate;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.Point;

@Getter
public class DrawMessage extends ServerMessage {

    @Delegate(types = DrawRequest.class)
    @Getter(AccessLevel.NONE)
    private final DrawRequest data;

    public DrawMessage(DrawRequest request) {
        super(ServerMessageType.DRAW_MESSAGE);
        data = request;
    }

    public DrawMessage(Point startPoint, Point finishPoint) {
        super(ServerMessageType.DRAW_MESSAGE);
        data = new DrawRequest(startPoint, finishPoint);
    }
}
