package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    void deleteAll();

    Optional<User> findByName(String name);
}
