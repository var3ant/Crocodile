package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.ChooseWordRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RoomService roomService;

    @MessageMapping("/chat/{roomId}")
    public void chat(@DestinationVariable long roomId, ChatRequest message, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        roomService.sendChatMessage(message, userId, roomId);
        var logger = LoggerFactory.getLogger(WebSocketController.class);
        logger.info(message.getMessage());
    }

    @MessageMapping("/choose_word/{roomId}")
    public void chooseWord(@DestinationVariable long roomId, ChooseWordRequest chooseWordRequest, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        roomService.chooseWord(userId, roomId, Integer.parseInt(chooseWordRequest.getIndex()));
    }

    @MessageMapping("/draw/{roomId}")
    public void draw(@DestinationVariable long roomId, DrawRequest draw, Principal principal) {
        var userId = Long.parseLong(principal.getName());
        roomService.sendDrawMessage(draw, userId, roomId);
    }

    //TODO: пока через отдельный метод, но нужно подумать, мб через эвентхэндлер. Надо понять откуда взять roomId.
    @MessageMapping("/join/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, Principal principal) throws Exception {
        try {
            var userId = Long.parseLong(principal.getName());
            var roomIdInt = Long.parseLong(roomId);
            roomService.join(userId, roomIdInt);
        } catch (NumberFormatException e) {
            throw new Exception("invalid id");
        }

    }

    @MessageMapping("/disconnect")
    public void disconnectRoom(Principal principal) {
        var userId = Long.parseLong(principal.getName());

        roomService.disconnect(userId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        var user = event.getUser();
        if (user == null) {
            return;//TODO:
        }
        var userId = Long.parseLong(user.getName());//TODO:exception

        roomService.disconnect(userId);
    }
}
