package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.service.ValidationUtils.checkId;
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
    public User getUser(String id) throws NotFoundException {
        return Optional.ofNullable(users.get(checkId(id))).orElseThrow(() -> new NotFoundException("Такого юзера нет"));

    }


    @Override
    public Map<Integer, User> getUsersMap() {
        return users;
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
