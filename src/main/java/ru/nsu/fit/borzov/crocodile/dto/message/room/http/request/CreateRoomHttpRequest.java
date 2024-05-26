package ru.nsu.fit.borzov.crocodile.dto.message.room.http.request;

import lombok.Data;

@Data
public class CreateRoomHttpRequest {
    private String name;
    private String password;
    private Long maxPlayers;//TODO: тут можно и int
}