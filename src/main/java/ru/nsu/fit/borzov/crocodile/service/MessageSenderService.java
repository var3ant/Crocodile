package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.ServerMessage;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;

@Service
@RequiredArgsConstructor
public class MessageSenderService {
    private final SimpMessagingTemplate template;
    private final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);

    public void sendToRoom(ServerMessage message, Room room) {
        logger.info("Sending to room {}: {}", room.getId(), message);
        template.convertAndSend("/topic/session/" + room.getId(), message);
    }

    public void sendToUser(ServerMessage message, User user) {
        logger.info("Sending to user {}: {}", user.getId(), message);
        template.convertAndSendToUser(user.getUsername(), "/queue/session", message);
    }
}
