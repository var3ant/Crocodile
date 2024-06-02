package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.*;
import ru.nsu.fit.borzov.crocodile.exception.*;
import ru.nsu.fit.borzov.crocodile.model.GuessingPhrase;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.GuessingPhraseRepository;
import ru.nsu.fit.borzov.crocodile.repository.RoomRepository;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GuessingPhraseRepository guessingPhraseRepository;
    private final MessageSenderService messageSenderService;
    private final Random random = new Random();//TODO: рандом защищен от параллельных запросов?

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public boolean join(long userId, long roomId) throws UserNotFoundException {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
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
            messageSenderService.sendToUser(new GetImageMessage(user.getId()), drawerBeforeUserConnect);
        }
        messageSenderService.sendToRoom(new InfoMessage(MessageFormat.format("User connected: {0}", user.getName())), room);
        return true;
    }

    public void sendChatMessage(ChatRequest message, long userId) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException {
        logger.info("chat message from {}: {}", userId, message.getMessage());

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

        messageSenderService.sendToRoom(new ChatMessage(message.getMessage(), user.getId()), room);
    }

    public void sendDrawMessage(DrawRequest draw, long userId) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException {
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
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        var room = user.getRoom();

        if (room != null) {
            var users = room.getUsers();

            user.setRoom(null);
            users.remove(user);//TODO: нужно ли оба действия делать?

            if (room.getDrawer() != null && user.getId() == room.getDrawer().getId()) {
                room.setDrawer(null);
                chooseNewDrawerIfNeeded(room);
            }

            if (users.isEmpty()) {
                room = roomRepository.save(room);
                roomRepository.delete(room);
            }
            messageSenderService.sendToRoom(new InfoMessage(user.getName() + " has been disconnected"), room);

        }

        userRepository.save(user);
    }

    public void chooseWord(long userId, int index) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException, IlligalRequestArgumentException {
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
            logger.error("Index of chosen word is out of bound");
            throw new IlligalRequestArgumentException("index");
        }

        room.setWord(wordsToChoose.get(index));
        room.setWordToChoose(new ArrayList<>());
        roomRepository.save(room);
    }

    private void chooseNewDrawerIfNeeded(Room room) {
        var minimumUsersToStartGame = 1;

        var users = room.getUsers();
        if (room.getDrawer() == null) {
            if (users.size() >= minimumUsersToStartGame) {
                chooseNewDrawer(room);
            } else {
                messageSenderService.sendToRoom(new InfoMessage("Waiting for players..."), room);
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

        var words = generatePhraseToChoose();
        room.setWordToChoose(words);
        roomRepository.save(room);
        messageSenderService.sendToRoom(new NewDrawerMessage(newDrawer.getId()), room);
        messageSenderService.sendToUser(new ChooseWordMessage(words), newDrawer);
    }

    private List<String> generatePhraseToChoose() {
        var count = guessingPhraseRepository.count();

        var phrases = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            var idx = random.nextInt((int) count);
            Page<GuessingPhrase> phrasePage = guessingPhraseRepository.findAll(PageRequest.of(idx, 1));
            var phraseOpt = phrasePage.stream().findFirst();
            if(phraseOpt.isPresent()) {
                phrases.add(phraseOpt.get().getPhrase());
            } else {
                logger.error("Phrases cannot be received");
                throw new InternalServerErrorException();
            }
        }

        return phrases;
    }

    public Room getById(long id) throws RoomNotFoundException {
        var roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            throw new RoomNotFoundException();
        }

        return roomOpt.get();
    }

    public long create(String name, String password, boolean isHidden) throws AlreadyExistException {
        if (roomRepository.existsByName(name)) {
            throw new AlreadyExistException();
        }

        var room = roomRepository.save(new Room(name, password, isHidden));
        logger.info("New room: {}", name);

        return room.getId();
    }

    public void sendImage(long senderId, String image, long receiverId) throws UserNotFoundException, UserNotInRoomException {
        var sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
        var receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);

        var senderRoom = sender.getRoom();
        if (senderRoom == null) {
            throw new UserNotInRoomException();
        }

        if (receiver.getRoom().getId() != senderRoom.getId()
                || senderRoom.getDrawer().getId() != sender.getId()) {
            messageSenderService.sendToUser(new ImageMessage(""), receiver);
            //TODO: Что делать, если рисующий выйдет из комнаты сразу как ему отправится запрос. Или если резко сменится рисующий.
            // Тут вообще много косяков может быть, это решает доп табличкой с инфой кто кому что должен.
            return;
        }

        messageSenderService.sendToUser(new ImageMessage(image), receiver);
    }

    public void clear(long userId) throws UserNotFoundException, UserNotInRoomException {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var room = user.getRoom();
        if (room == null) {
            throw new UserNotInRoomException();
        }
        messageSenderService.sendToRoom(new ClearMessage(), room);
    }
}
