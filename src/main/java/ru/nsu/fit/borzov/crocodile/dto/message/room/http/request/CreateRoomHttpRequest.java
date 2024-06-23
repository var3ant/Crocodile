package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoomHttpRequest {
    private @NotNull String name;
}
