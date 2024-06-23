package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.controller.util.PrincipalUtils;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.NameUserResponse;
import ru.nsu.fit.borzov.crocodile.exception.AuthenticationException;
import ru.nsu.fit.borzov.crocodile.exception.IllegalUserException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.service.FriendService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/friends/request")
@RequiredArgsConstructor
public class FriendRequestController {
    private final Logger logger = LoggerFactory.getLogger(FriendRequestController.class);
    private final PrincipalUtils principalUtils;

    private final FriendService friendService;

    @GetMapping("/sent")
    public List<NameUserResponse> getSentRequests(Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.getSentFriendRequests(userId);
    }

    @GetMapping("/received")
    public List<NameUserResponse> getReceivedRequests(Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        logger.info("Get received requests:{}", userId);
        return friendService.getReceivedFriendRequests(userId);
    }

    @PostMapping("/send/{anotherUserId}")
    public void sendFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException, IllegalUserException {
        var userId = principalUtils.getUserId(principal);

        friendService.sendFriendRequest(userId, anotherUserId);
    }

    @PostMapping("/decline/{anotherUserId}")
    public void declineFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        friendService.declineFriendRequest(userId, anotherUserId);
    }

    @PostMapping("/cancel/{anotherUserId}")
    public boolean cancelFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.cancelFriendRequest(userId, anotherUserId);
    }
}
