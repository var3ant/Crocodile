package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String password;
}
