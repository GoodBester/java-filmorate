package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) throws NotFoundException {
        return userService.getUser(id);
    }


    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable String id, @PathVariable String friendId) throws ValidationException, NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable String id, @PathVariable String friendId) throws ValidationException, NotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable String id) throws ValidationException, NotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable String id, @PathVariable String friendId) throws ValidationException, NotFoundException {
        return userService.getCommonFriends(id, friendId);
    }
}
