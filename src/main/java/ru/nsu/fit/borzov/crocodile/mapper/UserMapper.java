package ru.nsu.fit.borzov.crocodile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.LoginResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.RegisterRequest;
import ru.nsu.fit.borzov.crocodile.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    LoginResponse toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(RegisterRequest registerDto);

}