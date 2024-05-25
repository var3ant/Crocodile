package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.CheckAvailableConnectionHttpRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.request.CreateRoomHttpRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.*;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.RoomRepository;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: check user in room
//TODO: stop game when only a drawer in room?
@Service
@RequiredArgsConstructor
public class RoomService {
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final MessageSenderService messageSender;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();//TODO: рандом защищен от параллельных запросов?

//    public Long createRoom(String roomName) throws Exception {
//        var isExists = roomRepository.existsByName(roomName);
//
//        if (isExists) {
//            throw new Exception(); //TODO:
//        }
//        logger.info("new room: {}", roomName);
//        var room = roomRepository.save(new Room(roomName));
//        return room.getId();
//    }

    //    @Retryable
//    @Transactional(isolation = Isolation.SERIALIZABLE)
//    @Lock(LockModeType.OPTIMISTIC)
//    public Long createOrGetRoom(String roomName) throws Exception {
//        synchronized (this) {
//            logger.info("create or get room: {}", roomName);
//            var newRoom = roomRepository.findByName(roomName);
//
//            if (newRoom.isPresent()) {
//                logger.info("room present: {}", newRoom.get().getId());
//                return newRoom.get().getId();
//            }
//
//            return createRoom(roomName);
//        }
//    }

    public Long getRoom(String name) {
        var room = roomRepository.findByName(name);

        if (room == null) {
            return -1L;
        }
        return room.get().getId();
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public boolean join(long userId, long roomId) throws Exception {
        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new Exception();
        }

        var roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            return false;
        }

        var room = roomOptional.get();
        var user = userOptional.get();

        user.setRoom(room);
        room.getUsers().add(user);

        chooseNewDrawerIfNeeded(room);
        userRepository.save(user);
        roomRepository.save(room);
        return true;
    }

    public void sendChatMessage(ChatRequest message, long userId, long roomId) {
        logger.info("chat message from {}: {}", userId, message.getMessage());

        var roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            return;//TODO: error
        }

        if (roomOpt.get().getDrawer() != null && roomOpt.get().getDrawer().getId() == userId) {
            return;//TODO: error
        }

        if (message.getMessage().equals(roomOpt.get().getWord())) {
            setDrawer(roomOpt.get(), userRepository.findById(userId).get());//TODO: get nullable
        }

        messageSender.sendToSession(new ChatMessage(message.getMessage(), userId), roomId);
    }

    public void sendDrawMessage(DrawRequest draw, long userId, long roomId) {
        logger.info("draw from ({},{}) to ({},{})", draw.getStartPoint().getX(), draw.getStartPoint().getY(), draw.getFinishPoint().getX(), draw.getFinishPoint().getY());

        var roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty() || roomOpt.get().getDrawer().getId() != userId) {
            return;//TODO: error
        }

        messageSender.sendToSession(new DrawMessage(draw), roomId);
    }

    public void disconnect(long userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            //TODO: error
            return;
        }
        var user = userOpt.get();
        var room = user.getRoom();

        if (room != null) {
            var users = room.getUsers();

            user.setRoom(null);
            users.remove(user);//TODO: нужно ли оба действия делать?

            if (user.equals(room.getDrawer())) {
                room.setDrawer(null);
                chooseNewDrawerIfNeeded(room);
            }

            if (users.isEmpty()) {
                roomRepository.delete(room);
            }
            messageSender.sendToSession(new InfoMessage(user.getName() + " has been disconnected"), room.getId());

        }

        userRepository.save(user);
    }

    public void chooseWord(long userId, long roomId, int index) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            //TODO: error
            return;
        }

        var roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            //TODO: error
            return;
        }
        var room = roomOpt.get();

        if (!userOpt.get().getId().equals(room.getDrawer().getId())) {
            //TODO: error
            return;
        }

        var wordsToChoose = room.getWordToChoose();

        if (index < 0 || index >= wordsToChoose.size()) {
            //TODO: error
            return;
        }

        room.setWord(wordsToChoose.get(index));
        room.setWordToChoose(new ArrayList<>());
        roomRepository.save(room);
        //TODO: send to users?
    }

    private void chooseNewDrawerIfNeeded(Room room) {
        var users = room.getUsers();
        if (room.getDrawer() == null) {
            if (users.size() > 1) {
                chooseNewDrawer(room);
            } else {
                messageSender.sendToSession(new InfoMessage("Waiting for players..."), room.getId());
            }
        }
    }

    private void chooseNewDrawer(Room room) {
        var users = room.getUsers();

        var newDrawer = users.get(random.nextInt(users.size()));
        setDrawer(room, newDrawer);
    }

    private void setDrawer(Room room, User newDrawer) {
        room.setDrawer(newDrawer);

        var words = GenerateWordsToChoose(room);
        room.setWordToChoose(words);
        roomRepository.save(room);
        messageSender.sendToSession(new NewDrawerMessage(newDrawer.getId()), room.getId());
        messageSender.sendToUser(new ChooseWordMessage(words), newDrawer.getId().toString());
    }

    private List<String> GenerateWordsToChoose(Room room) {
        return List.of("word1", "word2", "word3");//TODO: list of words
    }

    public Long createWithoutName() {
        return null;//TODONOW:
    }

    public boolean isConnectionAvailable(long userId, long roomId, CheckAvailableConnectionHttpRequest connectionData) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            //TODO: error
            return false;
        }

        var roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            return false;
        }

        var room = roomOpt.get();
        if (connectionData.getPassword() != null) {
            return connectionData.getPassword().equals(room.getPassword());
        }

        return false;
    }

    private boolean isSessionFull(Room room) {//TODO: можно переместить в сам класс
        return room.getUsers().size() <= 10;
    }

    public Room getById(Long id) {

        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            return null;//TODO: not found
        }

        var room = roomOpt.get();
//        if(isSessionFull(room)) {
//            return null;//TODO: full
//        }

        return room;
    }

    public Long create(CreateRoomHttpRequest createRoomRequest) throws Exception {
        var isExists = roomRepository.existsByName(createRoomRequest.getName());

        if (isExists) {
            throw new Exception(); //TODO:
        }
        logger.info("new room: {}", createRoomRequest.getName());
        var room = roomRepository.save(new Room(createRoomRequest.getName(), createRoomRequest.getPassword()));//TODO:max players
        return room.getId();
    }
}
