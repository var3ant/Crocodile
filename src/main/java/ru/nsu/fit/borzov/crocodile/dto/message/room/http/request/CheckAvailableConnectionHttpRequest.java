package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request;

import lombok.Data;

@Data
public class CheckAvailableConnectionHttpRequest {
    private String password;
    private String code;
}
