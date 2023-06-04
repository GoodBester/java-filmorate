package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.validators.UserValidator.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;


    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }


    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        if (checkValid(user)) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый юзер");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (checkValid(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен новый юзер");
            return user;
        }

        throw new ValidationException("Такого фильма нет в базе");
    }
}
