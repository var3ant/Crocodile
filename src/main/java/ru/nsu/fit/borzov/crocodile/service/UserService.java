package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.LoginResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.LoginRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.RegisterRequest;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.exception.IlligalNameException;
import ru.nsu.fit.borzov.crocodile.exception.InvalidUserAuthDataException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.mapper.UserMapper;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.nio.CharBuffer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final String[] reservedNames = new String[]{"Admin", "Info"};

    private final JwtTokenUtil jwtTokenUtil;

    public Long register(RegisterRequest registerRequest) throws AlreadyExistException, IlligalNameException {

        var isExists = userRepository.existsByName(registerRequest.getLogin());

        if (isExists) {
            throw new AlreadyExistException();
        }

        if (StringUtils.equalsAnyIgnoreCase(registerRequest.getLogin(), reservedNames)) {
            throw new IlligalNameException();
        }

        var password = passwordEncoder.encode(CharBuffer.wrap(registerRequest.getPassword()));
        return userRepository.save(new User(registerRequest.getLogin(), password)).getId();
    }

    public void clearDb() {
        userRepository.deleteAll();
    }

    public List<Long> getUsers() {
        return userRepository.findAll().stream().map(User::getId).toList();
    }

//    public User findByLogin(String login) {
//        return userRepository.findByName(login);
//    }

    public LoginResponse login(LoginRequest loginRequest) throws InvalidUserAuthDataException {
        User user = userRepository.findByName(loginRequest.getLogin())
                .orElseThrow(InvalidUserAuthDataException::new);

        if (passwordEncoder.matches(CharBuffer.wrap(loginRequest.getPassword()), user.getPassword())) {
            var userDto = userMapper.toUserDto(user);
            userDto.setToken(jwtTokenUtil.createToken(user.getName()));
            return userDto;
        }

        throw new InvalidUserAuthDataException();
    }

    public User findByName(String userName) throws UserNotFoundException {
        return userRepository.findByName(userName).orElseThrow(UserNotFoundException::new);
    }
}