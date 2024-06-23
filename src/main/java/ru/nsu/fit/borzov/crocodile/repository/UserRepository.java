package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    void deleteAll();

    Optional<User> findByName(String name);

    //TODO: тут можно попрактиковать critera api
    @Query("select u from user_data u where u.name like CONCAT(:username,'%') and u.id != :actorId order by length(u.name)")
    List<User> findAllPotentialFriendsByNameStartingWith(long actorId, String username, Pageable pageable);
}
