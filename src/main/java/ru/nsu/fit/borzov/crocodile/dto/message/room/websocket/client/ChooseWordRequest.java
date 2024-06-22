package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChooseWordRequest {
    private @NotNull String index;
}
