package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.AccessLevel;
import lombok.Getter;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import lombok.experimental.Delegate;

@Getter
public class DrawMessage extends ServerMessage {

    @Delegate(types = DrawRequest.class)
    @Getter(AccessLevel.NONE)
    private final DrawRequest data;

    public DrawMessage(DrawRequest request) {
        super(ServerMessageType.DRAW_MESSAGE);
        data = request;
    }
}
