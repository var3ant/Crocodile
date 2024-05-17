package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.http.CheckAvailableConnection;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.http.CreateRoom;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.security.Principal;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

//    @PostMapping("create/{roomName}")
//    public Long creatOrGetRoom(@PathVariable String roomName) throws Exception {
//        return roomService.createOrGetRoom(roomName);
//    }

    @PostMapping("/create")
    public Long createRoom(@RequestBody CreateRoom createRoomRequest) throws Exception {
        return roomService.create(createRoomRequest);
    }

    @GetMapping("by_id/{id}")
    public Room getById(@PathVariable Long id) throws Exception {
        return roomService.getById(id);
    }

    @PostMapping("create_without_name/")
    public Long createRoomWithoutName() throws Exception {
        return roomService.createWithoutName();//TODONOW: return room dto
    }

    @PostMapping("check_available_connection/{roomId}")
    public boolean checkAvailableConnection(
            @PathVariable long roomId,
            CheckAvailableConnection checkAvailableConnectionDto,
            Principal principal) throws Exception {
        var userId = Long.parseLong(principal.getName());
        return roomService.isConnectionAvailable(userId, roomId, checkAvailableConnectionDto);
    }

    @PostMapping("/disconnect")
    public void disconnect(Principal principal) {
        var userId = Long.parseLong(principal.getName());
        roomService.disconnect(userId);
    }

    @GetMapping
    public List<Room> getRooms() {
        List<Room> rooms = roomService.getRooms();
        return rooms;
    }

    @GetMapping("/ping")
    public long ping() {
        return 1;
    }
}
