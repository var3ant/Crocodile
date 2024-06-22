package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String roomName);
}
