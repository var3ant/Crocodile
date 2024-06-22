package ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ChooseWordMessage extends ServerMessage {
    private final List<String> words;

    public ChooseWordMessage(@NotNull List<String> words) {
        super(ServerMessageType.CHOOSE_WORD_MESSAGE);
        this.words = words;
    }
}
