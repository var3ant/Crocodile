package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.FriendRequest;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findFirstByFromAndTo(User from, User to);

    List<FriendRequest> findAllByFrom(User from);

    List<FriendRequest> findAllByTo(User to);
}