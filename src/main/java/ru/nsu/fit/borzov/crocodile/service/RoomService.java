package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.*;
import ru.nsu.fit.borzov.crocodile.exception.*;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.RoomRepository;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final MessageSenderService messageSenderService;
    private final PhrasesService phrasesService;
    private final ConcurrentRandomService random;

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;



    private static final int MIN_USERS_TO_START_GAME = 1;

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public boolean join(long userId, long roomId) throws UserNotFoundException {
        logger.info("user {} joining the room {}", userId, roomId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            logger.info("Room {} not found", roomId);
            messageSenderService.sendToUser(new ConnectionError(roomId), user);
            return false;
        }
        var room = roomOptional.get();

        user.setRoom(room);
        room.getUsers().add(user);

        var drawerBeforeUserConnect = room.getDrawer();
        chooseNewDrawerIfNeeded(room);
        userRepository.save(user);
        roomRepository.save(room);

        if (drawerBeforeUserConnect != null) {
            logger.info("Sending request for the drawer {} in room {}", drawerBeforeUserConnect.getId(), roomId);
            messageSenderService.sendToUser(new GetImageMessage(user.getId()), drawerBeforeUserConnect);
        }

        logger.info("User {} joined to {}", userId, roomId);
        messageSenderService.sendToRoom(new InfoMessage(MessageFormat.format("User connected: {0}", user.getName())), room);
        return true;
    }

    public void sendChatMessage(@NotNull ChatRequest message, long userId) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException {
        logger.info("Chat message from {}: {}", userId, message.getMessage());

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }

        if (room.getDrawer() != null && room.getDrawer().getId() == userId) {
            throw new WrongGameRoleException();
        }

        if (room.getWord() != null && message.getMessage().equals(room.getWord())) {
            setDrawer(room, user);
        }

        messageSenderService.sendToRoom(new ChatMessage(user.getId(), user.getName(), message.getMessage()), room);
    }

    public void sendDrawMessage(@NotNull DrawRequest draw, long userId) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException {
        logger.info(
                "draw from ({},{}) to ({},{})",
                draw.getStartPoint().getX(),
                draw.getStartPoint().getY(),
                draw.getFinishPoint().getX(),
                draw.getFinishPoint().getY());

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }
        if (!Objects.equals(user.getId(), room.getDrawer().getId())) {
            throw new WrongGameRoleException();
        }

        messageSenderService.sendToRoom(new DrawMessage(draw), room);
    }

    public void disconnect(long userId) throws UserNotFoundException {
        logger.info("disconnecting user {}", userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var room = user.getRoom();

        if (room != null) {
            var users = room.getUsers();

            user.setRoom(null);
            users.remove(user);

            if (room.getDrawer() != null && user.getId() == room.getDrawer().getId()) {
                room.setDrawer(null);
                chooseNewDrawerIfNeeded(room);
            }

            if (users.isEmpty()) {
                room = roomRepository.save(room);
                user = userRepository.save(user);
                roomRepository.delete(room);
            }
            messageSenderService.sendToRoom(new InfoMessage(user.getName() + " has been disconnected"), room);

        }

        userRepository.save(user);
    }

    public void chooseWord(long userId, int index) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException, IlligalRequestArgumentException {
        logger.info("{} choose word {}", userId, index);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }

        if (user.getId() != room.getDrawer().getId()) {
            throw new WrongGameRoleException();
        }

        var wordsToChoose = room.getWordToChoose();

        if (index < 0 || index >= wordsToChoose.size()) {
            logger.warn("Index of chosen word is out of bound");
            throw new IlligalRequestArgumentException("index");
        }

        room.setWord(wordsToChoose.get(index));
        room.setWordToChoose(new ArrayList<>());
        roomRepository.save(room);
    }

    private void chooseNewDrawerIfNeeded(Room room) {
        var users = room.getUsers();
        if (room.getDrawer() == null) {
            if (users.size() >= MIN_USERS_TO_START_GAME) {
                chooseNewDrawer(room);
            } else {
                messageSenderService.sendToRoom(new InfoMessage("Waiting for players..."), room);
            }
        }
    }

    private void chooseNewDrawer(@NotNull Room room) {
        var users = room.getUsers();

        var newDrawer = users.get(random.nextInt(users.size()));
        setDrawer(room, newDrawer);
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

    public Room getById(long id) throws RoomNotFoundException {
        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            throw new RoomNotFoundException();
        }

        return roomOpt.get();
    }

    public long create(@NotNull String name) throws AlreadyExistException {
        logger.info("Creating room {}", name);
        if (roomRepository.existsByName(name)) {
            throw new AlreadyExistException();
        }

        var room = roomRepository.save(new Room(name));
        logger.info("New room: {}", name);

        return room.getId();
    }

    public void sendImage(long senderId, @NotNull String image, long receiverId) throws UserNotFoundException, UserNotInRoomException {
        logger.info("Sending image from {} to {}", senderId, receiverId);
        var sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        var receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);

        var senderRoom = sender.getRoom();
        if (senderRoom == null) {
            throw new UserNotInRoomException();
        }

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

    public void clearCanvas(long userId) throws UserNotFoundException, UserNotInRoomException {
        logger.info("Clear canvas request from {}", userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }
        messageSenderService.sendToRoom(new ClearMessage(), room);
    }

    public void reactToMessage(@NotNull String messageId, @NotNull ReactionType reaction, long userId) throws UserNotInRoomException, UserNotFoundException, WrongGameRoleException {
        logger.info("Reaction {} to message {} from {}", reaction.name(), messageId, userId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }

        if (room.getDrawer() != null && room.getDrawer().getId() != userId) {
            throw new WrongGameRoleException();
        }

        messageSenderService.sendToRoom(new ReactionMessage(messageId, reaction), room);
    }
}
