package ru.nsu.fit.borzov.crocodile.dto.message.room.client.http;

import lombok.Data;

@Data
public class CreateRoom {
    private String name;
    private String password;
    private Long maxPlayers;//TODO: тут можно и int
}
