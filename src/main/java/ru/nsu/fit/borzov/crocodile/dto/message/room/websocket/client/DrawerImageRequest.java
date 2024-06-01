package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

@Data
public class DrawerImageRequest {
    private String image;
    private long receiverId;

    DrawerImageRequest() {

    }

    DrawerImageRequest(String image, long receiverId) {
        this.image = image;
        this.receiverId = receiverId;
    }
}
