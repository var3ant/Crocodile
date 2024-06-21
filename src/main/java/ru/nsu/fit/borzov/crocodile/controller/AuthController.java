package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.LoginResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.LoginRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.user.RegisterRequest;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.exception.IllegalNameException;
import ru.nsu.fit.borzov.crocodile.exception.InvalidUserAuthDataException;
import ru.nsu.fit.borzov.crocodile.service.UserService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public long register(@RequestBody RegisterRequest registerRequest) throws AlreadyExistException, IllegalNameException {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws InvalidUserAuthDataException {
        return userService.login(loginRequest);
    }

    @GetMapping
    public List<Long> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/ping")
    public String pingString(@RequestParam String name) {
        return name;
    }
}
