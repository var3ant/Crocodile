package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.CreateRoomHttpRequest;
import ru.nsu.fit.borzov.crocodile.exception.AlreadyExistException;
import ru.nsu.fit.borzov.crocodile.exception.RoomNotFoundException;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public long createRoom(@RequestBody CreateRoomHttpRequest createRoomRequest) throws AlreadyExistException {
        return roomService.create(createRoomRequest.getName());
    }

    @GetMapping("by_id/{id}")
    public Room getById(@PathVariable long id) throws RoomNotFoundException {
        return roomService.getById(id);
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getRooms();
    }
}
