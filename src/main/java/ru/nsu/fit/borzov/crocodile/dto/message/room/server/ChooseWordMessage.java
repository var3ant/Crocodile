package ru.nsu.fit.borzov.crocodile.dto.message.room.server;


import lombok.Getter;

import java.util.List;

@Getter
public class ChooseWordMessage extends ServerMessage {
    private final List<String> words;

    public ChooseWordMessage(List<String> words) {
        super(ServerMessageType.CHOOSE_WORD_MESSAGE);
        this.words = words;
    }
}
