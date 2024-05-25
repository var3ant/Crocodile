package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.server.ServerMessage;

@Service
@RequiredArgsConstructor
public class MessageSenderService {
    private final SimpMessagingTemplate template;
    public void sendToSession(ServerMessage message, Long roomId){
        template.convertAndSend("/topic/session/" + roomId, message);
    }

    public void sendToUser(ServerMessage message, String name){
        template.convertAndSendToUser(name, "/queue/session", message);
    }
}
