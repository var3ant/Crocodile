package ru.nsu.fit.borzov.crocodile.dto.message.room.http.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PotentialFriendResponse {
    private @NotNull long id;
    private @NotNull String name;
    private boolean requestAlreadySent;
    private boolean alreadyFriend;
}
