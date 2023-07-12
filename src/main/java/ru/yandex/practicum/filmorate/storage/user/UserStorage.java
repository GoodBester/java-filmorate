package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers() throws NotFoundException;

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException, NotFoundException;

    User getUser(String id) throws NotFoundException, ValidationException;

    String addFriend(String id, String friendId) throws ValidationException, NotFoundException;

    String deleteFriend(String id, String friendId) throws ValidationException, NotFoundException;

    List<User> getFriends(String id) throws ValidationException, NotFoundException;

    List<User> getCommonFriends(String id, String friendId) throws ValidationException, NotFoundException;


}
