package ru.nsu.fit.borzov.crocodile.dto.message.room.http.response;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PotentialFriendResponse {
    private @NotNull long id;
    private @NotNull String name;
    private boolean requestAlreadySent;
    private boolean alreadyFriend;
}
