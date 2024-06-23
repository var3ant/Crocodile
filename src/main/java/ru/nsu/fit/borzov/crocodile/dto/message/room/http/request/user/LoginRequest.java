package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    private @NotNull String login;
    private @NotNull String password;
}
