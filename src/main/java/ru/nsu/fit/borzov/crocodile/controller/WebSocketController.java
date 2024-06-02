package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.nsu.fit.borzov.crocodile.controller.util.PrincipalUtils;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChatRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.ChooseWordRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawRequest;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.DrawerImageRequest;
import ru.nsu.fit.borzov.crocodile.exception.IlligalRequestArgumentException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotInRoomException;
import ru.nsu.fit.borzov.crocodile.exception.WrongGameRoleException;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RoomService roomService;
    private final PrincipalUtils principalUtils;

    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/chat")
    public void chat(ChatRequest message, Principal principal) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendChatMessage(message, userId);
    }

    @MessageMapping("/choose_word")
    public void chooseWord(ChooseWordRequest chooseWordRequest, Principal principal) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException, IlligalRequestArgumentException {
        var userId = principalUtils.getUserId(principal);
        roomService.chooseWord(userId, Integer.parseInt(chooseWordRequest.getIndex()));
    }

    @MessageMapping("/draw")
    public void draw(DrawRequest draw, Principal principal) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendDrawMessage(draw, userId);
    }

    @MessageMapping("/send_image")
    public void sendImage(DrawerImageRequest drawerImage, Principal principal) throws UserNotFoundException, UserNotInRoomException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendImage(userId, drawerImage.getImage(), drawerImage.getReceiverId());
    }

    @MessageMapping("/join/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, Principal principal) throws UserNotFoundException {
        try {
            var userId = principalUtils.getUserId(principal);
            var roomIdInt = Long.parseLong(roomId);
            roomService.join(userId, roomIdInt);
        } catch (NumberFormatException ignored) {
            logger.warn("Join request with wrong roomId type {}", roomId);
        }
    }

    @MessageMapping("/clear/")
    public void joinRoom(Principal principal) throws UserNotFoundException, UserNotInRoomException {
            var userId = principalUtils.getUserId(principal);
            roomService.clear(userId);
    }

    @MessageMapping("/disconnect")
    public void disconnectRoom(Principal principal) {
        var userId = principalUtils.getUserId(principal);

        try {
            roomService.disconnect(userId);
        } catch (UserNotFoundException e) {
            logger.error("User disconnect by message, but user doesnt exist {}", userId);
        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        var user = event.getUser();
        var userId = principalUtils.getUserId(user);

        try {
            roomService.disconnect(userId);
        } catch (UserNotFoundException e) {
            logger.error("User disconnect by session handler, but user doesnt exist {}", userId);
        }
    }
}
