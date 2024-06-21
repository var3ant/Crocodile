package ru.nsu.fit.borzov.crocodile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.FriendResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.LoginResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.RegisterRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.NameUserResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.PotentialFriendResponse;
import ru.nsu.fit.borzov.crocodile.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    LoginResponse toUserDto(User user);

    NameUserResponse toNameUserDto(User user);

    @Mapping(target = "roomId", source = "user.room.id")
    @Mapping(target = "roomName", source = "user.room.name")
    FriendResponse toFriendDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(RegisterRequest registerDto);
}