package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.LoginResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.LoginRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.RegisterRequest;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.exception.IllegalNameException;
import ru.nsu.fit.borzov.crocodile.exception.InvalidUserAuthDataException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.mapper.UserMapper;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private static final String[] RESERVED_NAMES = new String[]{"Admin", "Info"};


    public long register(RegisterRequest registerRequest) throws AlreadyExistException, IllegalNameException {
        logger.warn("Trying to register with login {}", registerRequest.getLogin());

        var isExists = userRepository.existsByName(registerRequest.getLogin());

        if (isExists) {
            throw new AlreadyExistException();
        }

        if (StringUtils.equalsAnyIgnoreCase(registerRequest.getLogin(), RESERVED_NAMES)) {
            throw new IllegalNameException();
        }

        var password = passwordEncoder.encode(CharBuffer.wrap(registerRequest.getPassword()));

        logger.warn("User {} registered", registerRequest.getLogin());
        return userRepository.save(new User(registerRequest.getLogin(), password)).getId();
    }

    public LoginResponse login(LoginRequest loginRequest) throws InvalidUserAuthDataException {
        User user = userRepository.findByName(loginRequest.getLogin())
                .orElseThrow(InvalidUserAuthDataException::new);

        if (passwordEncoder.matches(CharBuffer.wrap(loginRequest.getPassword()), user.getPassword())) {
            var userDto = userMapper.toUserDto(user);
            userDto.setToken(jwtTokenUtil.createToken(user.getName()));
            logger.warn("User {} with id {} logged", loginRequest.getLogin(), userDto.getId());
            return userDto;
        }

        throw new InvalidUserAuthDataException();
    }

    public User findByName(String userName) throws UserNotFoundException {
        return userRepository.findByName(userName).orElseThrow(UserNotFoundException::new);
    }
}