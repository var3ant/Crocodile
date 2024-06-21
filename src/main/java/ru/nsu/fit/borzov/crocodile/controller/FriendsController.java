package ru.nsu.fit.borzov.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.borzov.crocodile.controller.util.PrincipalUtils;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.FriendResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.NameUserResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.PotentialFriendResponse;
import ru.nsu.fit.borzov.crocodile.exception.AuthenticationException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.mapper.UserMapper;
import ru.nsu.fit.borzov.crocodile.service.FriendService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {
    private final FriendService friendService;
    private final UserMapper userMapper;
    private final PrincipalUtils principalUtils;
    private final Logger logger = LoggerFactory.getLogger(FriendsController.class);

    @GetMapping("/sent_requests")
    public List<NameUserResponse> getSentRequests(Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.getSentFriendRequests(userId);
    }

    @GetMapping("/received_requests")
    public List<NameUserResponse> getReceivedRequests(Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        logger.info("Get received requests:{}", userId);
        return friendService.getReceivedFriendRequests(userId);
    }

    @PostMapping("/send_request/{anotherUserId}")
    public void sendFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        friendService.sendFriendRequest(userId, anotherUserId);
    }

    @PostMapping("/decline_request/{anotherUserId}")
    public void declineFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        friendService.declineFriendRequest(userId, anotherUserId);
    }

    @PostMapping("/cancel_request/{anotherUserId}")
    public boolean cancelFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.cancelFriendRequest(userId, anotherUserId);
    }

    @PostMapping("/delete/{anotherUserId}")
    public void delete(@PathVariable Long anotherUserId, Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        friendService.delete(userId, anotherUserId);
    }

    @GetMapping
    public Collection<FriendResponse> getAll(Principal principal) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.getFriends(userId).stream().map(userMapper::toFriendDto).toList();
    }

    @GetMapping("/potential_by")
    public Collection<PotentialFriendResponse> potentialBy(Principal principal, @RequestParam String name) throws UserNotFoundException, AuthenticationException {
        var userId = principalUtils.getUserId(principal);

        return friendService.getPotentialFriendsByNameLike(userId, name);
    }
}
