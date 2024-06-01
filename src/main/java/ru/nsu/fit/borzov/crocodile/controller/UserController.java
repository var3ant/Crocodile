//package ru.nsu.fit.borzov.crocodile.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import ru.nsu.fit.borzov.crocodile.controller.util.PrincipalUtils;
//import ru.nsu.fit.borzov.crocodile.model.User;
//import ru.nsu.fit.borzov.crocodile.service.FriendService;
//
//import java.security.Principal;
//import java.util.Collection;
//
//@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class UserController {
//    private final FriendService friendService;
//
//    @PostMapping("/send_friend_request/{anotherUserId}")
//    public void setFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws Exception {
//        var userId = PrincipalUtils.getUserId(principal);
//
//        friendService.sendFriendRequest(userId, anotherUserId);
//    }
//
//    @PostMapping("/decline_friend_request/{anotherUserId}")
//    public void declineFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws Exception {
//        var userId = PrincipalUtils.getUserId(principal);
//
//        friendService.declineFriendRequest(userId, anotherUserId);
//    }
//
//    @PostMapping("/cancel_friend_request/{anotherUserId}")
//    public boolean cancelFriendRequest(@PathVariable Long anotherUserId, Principal principal) throws Exception {
//        var userId = PrincipalUtils.getUserId(principal);
//
//        return friendService.cancelFriendRequest(userId, anotherUserId);
//    }
//
//    @GetMapping("/friends")
//    public Collection<User> cancelFriendRequest(Principal principal) throws Exception {
//        var userId = PrincipalUtils.getUserId(principal);
//
//        return friendService.getFriends(userId);
//    }
//}
