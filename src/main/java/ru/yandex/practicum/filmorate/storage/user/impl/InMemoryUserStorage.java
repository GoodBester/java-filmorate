package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.service.ValidationUtils.checkUser;
import static ru.yandex.practicum.filmorate.validators.UserValidator.checkValid;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;


    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) throws NotFoundException {
        return Optional.ofNullable(users.get(id)).orElseThrow(() -> new NotFoundException("Такого юзера нет"));
    }

    @Override
    public String addFriend(int id, int friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, this);
        User friend = checkUser(friendId, this);
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

    @Override
    public String deleteFriend(int id, int friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, this);
        User friend = checkUser(friendId, this);
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

    @Override
    public List<User> getFriends(int id) throws NotFoundException, ValidationException {
        User user = checkUser(id, this);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriendsId())
            list.add(getUser(i));
        return list;
    }

    @Override
    public List<User> getCommonFriends(int id, int friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, this);
        User friend = checkUser(friendId, this);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (friend == null) {
            throw new NotFoundException("Такого друга нет");
        }
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriendsId()) {
            if (friend.getFriendsId().contains(i)) {
                list.add(getUser(i));
            }
        }
        return list;
    }


    @Override
    public User addUser(User user) throws ValidationException {
        if (checkValid(user)) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый юзер");
        }
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (checkValid(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен новый юзер");
            return user;
        }

        throw new ValidationException("Такого фильма нет в базе");
    }
}
