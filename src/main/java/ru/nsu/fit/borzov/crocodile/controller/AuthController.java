package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.service.UserService;

import java.util.List;

//FIXME: затычка вместо авторизации, чтобы пока заняться самой игрой. Нужно будет заменить.
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/clear")
    public void clear() {
        userService.clearDb();
    }

    @PostMapping("/signup/{name}")
    public long signUp(@PathVariable String name) throws AlreadyExistException {
        return userService.createNew(name);
    }

    @ExceptionHandler({AlreadyExistException.class})
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exist");
    }

    @GetMapping
    public List<Long> getUsers() {
        return userService.getUsers();
    }
}
