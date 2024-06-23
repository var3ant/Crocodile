package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DrawerImageRequest {
    private @NotNull String image;
    private long receiverId;
}
