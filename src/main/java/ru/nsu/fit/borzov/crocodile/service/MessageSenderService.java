package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.ServerMessage;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.model.User;

@Service
@RequiredArgsConstructor
public class MessageSenderService {
    private final SimpMessagingTemplate template;

    public void sendToRoom(ServerMessage message, Room room) {
        template.convertAndSend("/topic/session/" + room.getId(), message);
    }

    public void sendToUser(ServerMessage message, User user) {
        template.convertAndSendToUser(user.getUsername(), "/queue/session", message);
    }
}
