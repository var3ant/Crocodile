package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.FriendResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.NameUserResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.PotentialFriendResponse;
import ru.nsu.fit.borzov.crocodile.exception.IllegalUserException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.mapper.UserMapper;
import ru.nsu.fit.borzov.crocodile.model.FriendRequest;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.FriendRequestRepository;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final Logger logger = getLogger(FriendService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;

    private static final int COUNT_RESULTS_FOR_BY_NAME_REQUEST = 10;

    @Transactional
    public void sendFriendRequest(long userId, long anotherUserId) throws UserNotFoundException, IllegalUserException {
        logger.info("Friend request from {} to {}", userId, anotherUserId);

        if (userId == anotherUserId) {
            throw new IllegalUserException();
        }

        User user = userService.findUserOrThrow(userId);
        User anotherUser = userService.findUserOrThrow(anotherUserId);

        var inverseRequest = friendRequestRepository.findFirstByFromAndTo(anotherUser, user);
        if (inverseRequest.isPresent()) {
            logger.info("Found outcomming request while sending incoming request from {} to {}. Adding to friends", userId, anotherUserId);
            user.getFriends().add(anotherUser);
            anotherUser.getFriends().add(user);
            friendRequestRepository.delete(inverseRequest.get());
            return;
        }

        logger.info("Friend request from {} to {} was saved", userId, anotherUserId);
        friendRequestRepository.save(new FriendRequest(user, anotherUser));
    }

    @Transactional
    public void declineFriendRequest(long userId, long anotherUserId) throws UserNotFoundException {
        logger.info("Decline friend request from {} to {}", anotherUserId, userId);

        User user = userService.findUserOrThrow(userId);
        User anotherUser = userService.findUserOrThrow(anotherUserId);

        var request = friendRequestRepository.findFirstByFromAndTo(anotherUser, user);
        request.ifPresent(friendRequestRepository::delete);
    }

    @Transactional
    public boolean cancelFriendRequest(long userId, long anotherUserId) throws UserNotFoundException {
        logger.info("Cancel friend request from {} to {}", userId, anotherUserId);

        User user = userService.findUserOrThrow(userId);
        User anotherUser = userService.findUserOrThrow(anotherUserId);

        var request = friendRequestRepository.findFirstByFromAndTo(user, anotherUser);

        if (request.isPresent()) {
            friendRequestRepository.delete(request.get());
            return true;
        }

        return false;
    }

    @Transactional
    public List<FriendResponse> getFriends(long userId) throws UserNotFoundException {
        logger.info("Getting friends for {}", userId);

        User user = userService.findUserOrThrow(userId);

        return user.getFriends().stream().map(userMapper::toFriendDto).toList();
    }

    @Transactional
    public void delete(long userId, long anotherUserId) throws UserNotFoundException {
        logger.info("Deleting user {} from friend of user {}", anotherUserId, userId);

        User user = userService.findUserOrThrow(userId);
        User anotherUser = userService.findUserOrThrow(anotherUserId);

        var foundOnAnotherUser = anotherUser.getFriends().remove(user);
        var foundOnUser = user.getFriends().remove(anotherUser);

        if (!foundOnAnotherUser || !foundOnUser) {
            logger.warn("Cant delete user {} from friends of {}. User not found.", anotherUserId, userId);
        }

//        userRepository.save(user);
//        userRepository.save(anotherUser);
    }

    @Transactional
    public List<NameUserResponse> getSentFriendRequests(long userId) throws UserNotFoundException {
        logger.info("Get sent friend requests for {}", userId);
        User user = userService.findUserOrThrow(userId);

        var requests = friendRequestRepository.findAllByFrom(user);

        return requests.stream().map(FriendRequest::getTo).map(userMapper::toNameUserDto).toList();
    }

    @Transactional
    public List<NameUserResponse> getReceivedFriendRequests(long userId) throws UserNotFoundException {
        logger.info("Get received friend requests for {}", userId);

        User user = userService.findUserOrThrow(userId);

        var requests = friendRequestRepository.findAllByTo(user);

        return requests.stream().map(FriendRequest::getFrom).map(userMapper::toNameUserDto).toList();
    }

    @Transactional
    public List<PotentialFriendResponse> getPotentialFriendsByNameLike(long userId, String name) throws UserNotFoundException {
        logger.info("Get potential friends by name for {} and name '{}'", userId, name);

        User user = userService.findUserOrThrow(userId);

        logger.info("Search for user with name like: {}", name);

        var users = userRepository.findAllPotentialFriendsByNameStartingWith(user.getId(), name, PageRequest.of(0, COUNT_RESULTS_FOR_BY_NAME_REQUEST));
        return users.stream().map(potentialFriend -> toPotentialFriend(user, potentialFriend)).toList();
    }

    private PotentialFriendResponse toPotentialFriend(User user, User potentialFriend) {
        var response = new PotentialFriendResponse();
        response.setId(potentialFriend.getId());
        response.setName(potentialFriend.getName());

        var alreadySent = friendRequestRepository.findFirstByFromAndTo(user, potentialFriend).isPresent();
        var alreadyFriend = user.getFriends().contains(potentialFriend);

        response.setRequestAlreadySent(alreadySent);
        response.setAlreadyFriend(alreadyFriend);
        return response;
    }
}
