//package ru.nsu.fit.borzov.crocodile.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
//import ru.nsu.fit.borzov.crocodile.model.FriendRequest;
//import ru.nsu.fit.borzov.crocodile.model.User;
//import ru.nsu.fit.borzov.crocodile.repository.FriendRequestRepository;
//import ru.nsu.fit.borzov.crocodile.repository.UserRepository;
//
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class FriendService {
//    private final UserRepository userRepository;
//    private final FriendRequestRepository friendRequestRepository;
//
//    public void sendFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(UserNotFoundException::new);
//        User anotherUser = userRepository.findById(anotherUserId)
//                .orElseThrow(UserNotFoundException::new);
//
//        var inverseRequestId = friendRequestRepository.findByRequestFromAndRequestTo(anotherUser, user);
//        if (inverseRequestId.isPresent()) {
//            user.getFollowing().add(anotherUser);
//            anotherUser.getFollowers().add(user);
//            userRepository.save(user);
//            userRepository.save(anotherUser);
//            friendRequestRepository.deleteById(inverseRequestId.get());
//            return;
//        }
//
//        friendRequestRepository.save(new FriendRequest(user, anotherUser));
//    }
//
//    public void declineFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(UserNotFoundException::new);
//        User anotherUser = userRepository.findById(anotherUserId)
//                .orElseThrow(UserNotFoundException::new);
//
//        var requestId = friendRequestRepository.findByRequestFromAndRequestTo(anotherUser, user);
//        requestId.ifPresent(friendRequestRepository::deleteById);
//    }
//
//    public boolean cancelFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(UserNotFoundException::new);
//        User anotherUser = userRepository.findById(anotherUserId)
//                .orElseThrow(UserNotFoundException::new);
//
//        var requestId = friendRequestRepository.findByRequestFromAndRequestTo(user, anotherUser);
//
//        if (requestId.isPresent()) {
//            friendRequestRepository.deleteById(requestId.get());
//            return true;
//        }
//
//        return false;
//    }
//
//    public Set<User> getFriends(Long userId) throws UserNotFoundException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(UserNotFoundException::new);
//
//        return user.getFollowers();//TODO: маппер
//    }
//}
