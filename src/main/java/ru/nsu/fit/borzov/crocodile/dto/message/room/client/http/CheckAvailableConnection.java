package ru.nsu.fit.borzov.crocodile.dto.message.room.client.http;

import lombok.Data;

@Data
public class CheckAvailableConnection {
    private String password;
    private String code;
}
