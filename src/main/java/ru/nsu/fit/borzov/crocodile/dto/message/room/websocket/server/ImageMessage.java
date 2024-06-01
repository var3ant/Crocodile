package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;

import lombok.Getter;

@Getter
public class ImageMessage extends ServerMessage {
    private final String image;

    public ImageMessage(String image) {
        super(ServerMessageType.IMAGE_MESSAGE);
        this.image = image;
    }
}
