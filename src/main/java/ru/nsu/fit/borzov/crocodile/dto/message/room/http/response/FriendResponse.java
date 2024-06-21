package ru.nsu.fit.borzov.crocodile.dto.message.room.http.response;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Data
public class FriendResponse {
    private @NotNull long id;
    private @NotNull String name;
    private @Nullable Long roomId;
    private @Nullable String roomName;
}
