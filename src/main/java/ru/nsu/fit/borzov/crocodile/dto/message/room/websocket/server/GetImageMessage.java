package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class GetImageMessage extends ServerMessage {
    private final long receiverId;

    public GetImageMessage(long receiverId) {
        super(ServerMessageType.GET_IMAGE_MESSAGE);
        this.receiverId = receiverId;
    }
}
