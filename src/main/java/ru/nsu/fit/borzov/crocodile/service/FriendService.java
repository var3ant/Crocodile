package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.config.AfterStartupActions;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.NameUserResponse;
import ru.nsu.fit.borzov.crocodile.dto.message.room.http.response.PotentialFriendResponse;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.mapper.UserMapper;
import ru.nsu.fit.borzov.crocodile.model.FriendRequest;
import ru.nsu.fit.borzov.crocodile.model.User;
import ru.nsu.fit.borzov.crocodile.repository.FriendRequestRepository;
import ru.nsu.fit.borzov.crocodile.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserMapper userMapper;
    private final Logger logger = getLogger(FriendService.class);


    public void sendFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User anotherUser = userRepository.findById(anotherUserId)
                .orElseThrow(UserNotFoundException::new);

        var inverseRequest = friendRequestRepository.findFirstByFromAndTo(anotherUser, user);
        if (inverseRequest.isPresent()) {
            user.getFriends().add(anotherUser);
            anotherUser.getFriends().add(user);
            userRepository.save(user);
            userRepository.save(anotherUser);
            friendRequestRepository.delete(inverseRequest.get());
            return;
        }

        friendRequestRepository.save(new FriendRequest(user, anotherUser));
    }

    public void declineFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User anotherUser = userRepository.findById(anotherUserId)
                .orElseThrow(UserNotFoundException::new);

        var request = friendRequestRepository.findFirstByFromAndTo(anotherUser, user);
        request.ifPresent(friendRequestRepository::delete);
    }

    public boolean cancelFriendRequest(Long userId, Long anotherUserId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User anotherUser = userRepository.findById(anotherUserId)
                .orElseThrow(UserNotFoundException::new);

        var request = friendRequestRepository.findFirstByFromAndTo(user, anotherUser);

        if (request.isPresent()) {
            friendRequestRepository.delete(request.get());
            return true;
        }

        return false;
    }

    public Set<User> getFriends(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return user.getFriends();
    }

    public void delete(long userId, Long anotherUserId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User anotherUser = userRepository.findById(anotherUserId)
                .orElseThrow(UserNotFoundException::new);

        anotherUser.getFriends().removeIf(friend -> friend.getId() == userId);
        user.getFriends().removeIf(friend -> friend.getId() == anotherUserId);

        userRepository.save(user);
        userRepository.save(anotherUser);
    }

    public List<NameUserResponse> getSentFriendRequests(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var requests = friendRequestRepository.findAllByFrom(user);

        return requests.stream().map(FriendRequest::getTo).map(userMapper::toNameUserDto).toList();
    }

    public List<NameUserResponse> getReceivedFriendRequests(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var requests = friendRequestRepository.findAllByTo(user);

        return requests.stream().map(FriendRequest::getFrom).map(userMapper::toNameUserDto).toList();
    }

    public List<PotentialFriendResponse> getPotentialFriendsByNameLike(long userId, String name) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        logger.info("Search for user with name like: {}", name);
        var users = userRepository.findAllByNameStartingWith(name, PageRequest.of(0, 10));
        return users.stream().map((potentialFriend) -> toPotentialFriend(user, potentialFriend)).toList();
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
