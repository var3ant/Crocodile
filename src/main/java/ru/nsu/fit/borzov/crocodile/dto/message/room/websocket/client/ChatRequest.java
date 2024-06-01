package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRequest {

    @NotNull
    private String message;
}
