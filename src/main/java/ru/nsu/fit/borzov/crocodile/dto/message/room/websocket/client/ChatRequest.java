package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
}