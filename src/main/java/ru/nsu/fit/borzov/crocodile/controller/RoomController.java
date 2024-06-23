package ru.nsu.fit.borzov.crocodile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.CreateRoomHttpRequest;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.exception.RoomNotFoundException;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public long createRoom(@Valid @RequestBody CreateRoomHttpRequest createRoomRequest) throws AlreadyExistException {
        return roomService.create(createRoomRequest.getName());
    }

    @GetMapping("/{id}")
    public Room getById(@PathVariable long id) throws RoomNotFoundException {
        return roomService.findOrThrow(id);
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getRooms();
    }
}
