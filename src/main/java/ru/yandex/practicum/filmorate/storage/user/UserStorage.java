package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> getUsers();

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    User getUser(String id) throws NotFoundException;

    Map<Integer, User> getUsersMap();
}
