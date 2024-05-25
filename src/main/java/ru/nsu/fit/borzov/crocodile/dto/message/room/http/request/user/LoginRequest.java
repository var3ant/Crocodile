package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
