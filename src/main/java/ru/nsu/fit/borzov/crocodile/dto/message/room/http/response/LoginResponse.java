package ru.nsu.fit.borzov.crocodile.dto.message.room.http.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginResponse {
    private long id;
    private @NotNull String token;
}
