package ru.nsu.fit.borzov.crocodile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.nsu.fit.borzov.crocodile.controller.util.PrincipalUtils;
import ru.nsu.fit.borzov.crocodile.dto.message.room.websocket.client.*;
import ru.nsu.fit.borzov.crocodile.exception.*;
import ru.nsu.fit.borzov.crocodile.service.RoomService;

import java.security.Principal;

@Validated
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    private final PrincipalUtils principalUtils;

    private final RoomService roomService;

    @MessageMapping("/chat")
    public void chat(@Valid ChatRequest message, Principal principal) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendChatMessage(message, userId);
    }

    @MessageMapping("/react_to_message")
    public void reactToMessage(@Valid ReactionRequest reactionRequest, Principal principal) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.reactToMessage(reactionRequest.getMessageId(), reactionRequest.getReaction(), userId);
    }

    @MessageMapping("/choose_word")
    public void chooseWord(@Valid ChooseWordRequest chooseWordRequest, Principal principal) throws UserNotFoundException, UserNotInRoomException, WrongGameRoleException, IllegalRequestArgumentException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.chooseWord(userId, Integer.parseInt(chooseWordRequest.getIndex()));
    }

    @MessageMapping("/draw")
    public void draw(@Valid DrawRequest draw, Principal principal) throws UserNotFoundException, WrongGameRoleException, UserNotInRoomException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendDrawMessage(draw, userId);
    }

    @MessageMapping("/send_image")
    public void sendImage(@Valid DrawerImageRequest drawerImage, Principal principal) throws UserNotFoundException, UserNotInRoomException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.sendImage(userId, drawerImage.getImage(), drawerImage.getReceiverId());
    }

    @MessageMapping("/join/{roomId}")
    public void joinRoom(@DestinationVariable long roomId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.join(userId, roomId);
    }

    @MessageMapping("/clear")
    public void clear(Principal principal) throws UserNotFoundException, UserNotInRoomException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);
        roomService.clearCanvas(userId);
    }

    @MessageMapping("/disconnect")
    public void disconnectRoom(Principal principal) throws AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        try {
            roomService.disconnect(userId);
        } catch (UserNotFoundException e) {
            logger.error("User disconnect by message, but user doesnt exist {}", userId);
        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) throws AuthenticationException {
        var user = event.getUser();
        var userId = principalUtils.getUserId(user);

        try {
            roomService.disconnect(userId);
        } catch (UserNotFoundException e) {
            logger.error("User disconnect by session handler, but user doesnt exist {}", userId);
        }
    }
}
