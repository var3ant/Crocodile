package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private int x;
    private int y;
}
