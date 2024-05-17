package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long createNew(String name) throws AlreadyExistException {
        var isExists = userRepository.existsByName(name);

        if(isExists) {
            throw new AlreadyExistException();
        }
        return userRepository.save(new User(name)).getId();
    }

    public void clearDb() {
        userRepository.deleteAll();
    }

    public List<Long> getUsers() {
        return userRepository.findAll().stream().map(User::getId).toList();
    }
}
