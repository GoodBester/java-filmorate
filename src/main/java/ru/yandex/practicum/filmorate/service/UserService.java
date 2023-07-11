package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
public class UserService {


    protected UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() throws NotFoundException {
        return userStorage.getUsers();
    }

    public User getUser(String id) throws NotFoundException, ValidationException {
        return userStorage.getUser(id);
    }

    public User addUser(User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException, NotFoundException {
        return userStorage.updateUser(user);
    }

    public String addFriend(String id, String friendId) throws ValidationException, NotFoundException {
        return userStorage.addFriend(id, friendId);
    }

    public String deleteFriend(String id, String friendId) throws ValidationException, NotFoundException {
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(String id) throws ValidationException, NotFoundException {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(String id, String friendId) throws ValidationException, NotFoundException {
        return userStorage.getCommonFriends(id, friendId);
    }

}
