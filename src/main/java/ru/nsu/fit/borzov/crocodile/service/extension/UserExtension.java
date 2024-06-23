package ru.nsu.fit.borzov.crocodile.service.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.nsu.fit.borzov.crocodile.exception.UserNotInRoomException;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserExtension {

    public static Room getRoomOrThrow(User user) throws UserNotInRoomException {
        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }
        return room;
    }
}
