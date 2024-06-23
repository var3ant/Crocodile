package ru.nsu.fit.borzov.crocodile.service.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.nsu.fit.borzov.crocodile.exception.WrongGameRoleException;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomExtension {

    public static boolean isDrawer(Room room, User user) {
        return Objects.equals(user.getId(), room.getDrawer().getId());
    }

    public static void validateDrawer(Room room, User user) throws WrongGameRoleException {
        if(!isDrawer(room, user)) {
            throw new WrongGameRoleException();
        }
    }

    public static void validateNotDrawer(Room room, User user) throws WrongGameRoleException {
        if(isDrawer(room, user)) {
            throw new WrongGameRoleException();
        }
    }
}
