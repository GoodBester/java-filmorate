package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers() throws NotFoundException;

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException, NotFoundException;

    User getUser(int id) throws NotFoundException, ValidationException;

    String addFriend(int id, int friendId) throws ValidationException, NotFoundException;

    String deleteFriend(int id, int friendId) throws ValidationException, NotFoundException;

    List<User> getFriends(int id) throws ValidationException, NotFoundException;

    List<User> getCommonFriends(int id, int friendId) throws ValidationException, NotFoundException;


}
