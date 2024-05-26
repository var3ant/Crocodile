package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.FriendRequest;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<Long> findByRequestFromAndRequestTo(User from, User to);
}