package ru.nsu.fit.borzov.crocodile.dto.message.room.http.response;

import lombok.Data;

@Data
public class LoginResponse {
    private long id;
    private String token;
}
