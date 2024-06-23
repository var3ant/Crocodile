package ru.nsu.fit.borzov.crocodile.service;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.*;
import ru.nsu.fit.borzov.crocodile.exception.*;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.RoomRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.borzov.crocodile.service.extension.RoomExtension.*;
import static ru.nsu.fit.borzov.crocodile.service.extension.UserExtension.getRoomOrThrow;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final ConcurrentRandomService random;

    private final MessageSenderService messageSenderService;
    private final PhrasesService phrasesService;
    private final UserService userService;

    private final RoomRepository roomRepository;


    private static final int MIN_USERS_TO_START_GAME = 1;

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public void join(long userId, long roomId) throws UserNotFoundException {
        logger.info("user {} joining the room {}", userId, roomId);

        var user = userService.findUserOrThrow(userId);
        var roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            logger.info("Room {} not found", roomId);
            messageSenderService.sendToUser(new ConnectionError(roomId), user);
            return;
        }
        var room = roomOptional.get();

        userService.setRoom(user, room);
        room.getUsers().add(user);

        var drawer = room.getDrawer();
        if (drawer != null) {
            logger.info("Sending request for the drawer {} in room {}", drawer.getId(), roomId);
            messageSenderService.sendToUser(new GetImageMessage(user.getId()), drawer);
        } else {
            findNewDrawer(room);
            roomRepository.save(room);
        }

        logger.info("User {} joined to {}", userId, roomId);
        messageSenderService.sendToRoom(new InfoMessage(MessageFormat.format("User connected: {0}", user.getName())), room);
    }

    @Transactional
    public void sendChatMessage(@NonNull ChatRequest message, long userId) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException {
        logger.info("Chat message from {}: {}", userId, message.getMessage());

        var user = userService.findUserOrThrow(userId);

        var room = getRoomOrThrow(user);

        validateNotDrawer(room, user);

        if (room.getWord() != null && message.getMessage().equals(room.getWord())) {
            setDrawer(room, user);
        }

        messageSenderService.sendToRoom(new ChatMessage(user.getId(), user.getName(), message.getMessage()), room);
    }

    @Transactional
    public void sendDrawMessage(@Nonnull DrawRequest draw, long userId) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException {
        logger.info(
                "draw from ({},{}) to ({},{})",
                draw.getStartPoint().getX(),
                draw.getStartPoint().getY(),
                draw.getFinishPoint().getX(),
                draw.getFinishPoint().getY());

        var user = userService.findUserOrThrow(userId);
        var room = getRoomOrThrow(user);

        validateDrawer(room, user);

        messageSenderService.sendToRoom(new DrawMessage(draw), room);
    }

    @Transactional
    public void disconnect(long userId) throws UserNotFoundException {
        logger.info("disconnecting user {}", userId);

        var user = userService.findUserOrThrow(userId);

        var room = user.getRoom();
        if (room == null) {
            return;
        }

        var users = room.getUsers();

        user.setRoom(null);
        users.remove(user);


        if (isDrawer(room, user)) {
            room.setDrawer(null);
            findNewDrawer(room);
        }

        if (users.isEmpty()) {
            logger.info("room {} has beed deleted", room.getId());
            roomRepository.delete(room);
        }

        messageSenderService.sendToRoom(new InfoMessage(user.getName() + " has been disconnected"), room);
    }

    @Transactional
    public void chooseWord(long userId, int index) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException, IllegalRequestArgumentException {
        logger.info("{} choose word {}", userId, index);

        var user = userService.findUserOrThrow(userId);
        var room = getRoomOrThrow(user);

        validateDrawer(room, user);

        var wordsToChoose = room.getWordToChoose();
        if (index < 0 || index >= wordsToChoose.size()) {
            logger.warn("Index of chosen word is out of bound");
            throw new IllegalRequestArgumentException("index");
        }

        room.setWord(wordsToChoose.get(index));
        room.setWordToChoose(new ArrayList<>());
        roomRepository.save(room);
    }

    public Room findOrThrow(long id) throws RoomNotFoundException {
        return roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    @Transactional
    public long create(@NonNull String name) throws AlreadyExistException {
        logger.info("Creating room {}", name);
        if (roomRepository.existsByName(name)) {
            throw new AlreadyExistException();
        }

        var room = roomRepository.save(new Room(name));
        logger.info("New room: {}", name);

        return room.getId();
    }

    @Transactional
    public void sendImage(long senderId, @NonNull String image, long receiverId) throws UserNotFoundException, UserNotInRoomException {
        logger.info("Sending image from {} to {}", senderId, receiverId);
        var sender = userService.findUserOrThrow(senderId);
        var receiver = userService.findUserOrThrow(receiverId);

        var senderRoom = getRoomOrThrow(sender);

        if (receiver.getRoom().getId() != senderRoom.getId()
                || senderRoom.getDrawer().getId() != sender.getId()) {

            logger.info("Empty image from {} to {} sent", senderId, receiverId);
            messageSenderService.sendToUser(new ImageMessage(""), receiver);
            //TODO: Что делать, если рисующий выйдет из комнаты сразу как ему отправится запрос. Или если резко сменится рисующий.
            // Тут вообще много косяков может быть,
            // это решается доп табличкой с инфой что сервер должен отправить рисунок когда он придет и пачку сообщений о рисовании.
            return;
        }

        messageSenderService.sendToUser(new ImageMessage(image), receiver);
    }

    @Transactional
    public void clearCanvas(long userId) throws UserNotFoundException, UserNotInRoomException {
        logger.info("Clear canvas request from {}", userId);

        var user = userService.findUserOrThrow(userId);
        var room = getRoomOrThrow(user);

        messageSenderService.sendToRoom(new ClearMessage(), room);
    }

    @Transactional
    public void reactToMessage(@NonNull String messageId, @NonNull ReactionType reaction, long userId) throws UserNotInRoomException, UserNotFoundException, WrongGameRoleException {
        logger.info("Reaction {} to message {} from {}", reaction.name(), messageId, userId);
        var user = userService.findUserOrThrow(userId);
        var room = getRoomOrThrow(user);

        validateDrawer(room, user);

        messageSenderService.sendToRoom(new ReactionMessage(messageId, reaction), room);
    }

    private void findNewDrawer(Room room) {
        assert room.getDrawer() == null;

        var users = room.getUsers();
        if (users.size() >= MIN_USERS_TO_START_GAME) {
            var newDrawer = users.get(random.nextInt(users.size()));
            setDrawer(room, newDrawer);
        } else {
            messageSenderService.sendToRoom(new InfoMessage("Waiting for players..."), room);
        }
    }

    private void setDrawer(Room room, User newDrawer) {
        logger.info("New drawer {} of room {}", newDrawer.getId(), room.getId());
        room.setDrawer(newDrawer);

        var words = phrasesService.generatePhraseToChoose(room);
        room.setWordToChoose(words);
        roomRepository.save(room);
        messageSenderService.sendToRoom(new NewDrawerMessage(newDrawer.getId(), newDrawer.getName()), room);
        messageSenderService.sendToUser(new ChooseWordMessage(words), newDrawer);
    }
}
