package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.ValidationUtils.checkUser;


@Service
public class UserService {


    protected UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(String id) throws NotFoundException {
        return userStorage.getUser(id);
    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    public String addFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (friend == null) {
            throw new NotFoundException("Такого друга нет");
        }
        user.getFriendsId().add(friend.getId());
        friend.getFriendsId().add(user.getId());
        return String.format("Теперь %s друзья с %s",
                user.getName(), friend.getName());
    }

    public String deleteFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (friend == null) {
            throw new NotFoundException("Такого друга нет");
        }
        user.getFriendsId().remove(friend.getId());
        friend.getFriendsId().remove(user.getId());
        return String.format("Теперь %s больше не друзья с %s",
                user.getName(), friend.getName());
    }

    public List<User> getFriends(String id) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriendsId())
            list.add(userStorage.getUsersMap().get(i));
        return list;
    }

    public List<User> getCommonFriends(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (friend == null) {
            throw new NotFoundException("Такого друга нет");
        }
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriendsId()) {
            if (friend.getFriendsId().contains(i)) {
                list.add(userStorage.getUsersMap().get(i));
            }
        }
        return list;
    }

}
