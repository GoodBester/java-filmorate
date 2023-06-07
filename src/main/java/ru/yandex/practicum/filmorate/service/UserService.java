package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.Servicing.checkUser;


@Service
public class UserService {


    protected UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null || friend == null) throw new NotFoundException("Обьект не найден");

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return String.format("Теперь %s друзья с %s",
                user.getName(), friend.getName());
    }

    public String deleteFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null || friend == null) throw new NotFoundException("Обьект не найден");

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return String.format("Теперь %s больше не друзья с %s",
                user.getName(), friend.getName());
    }

    public List<User> getFriends(String id) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        if (user == null) throw new NotFoundException("Обьект не найден");
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriends())
            list.add(userStorage.getUsersMap().get(i));
        return list;
    }

    public List<User> getCommonFriends(String id, String friendId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        User friend = checkUser(friendId, userStorage);
        if (user == null || friend == null) throw new NotFoundException("Обьект не найден");
        List<User> list = new ArrayList<>();
        for (Integer i : user.getFriends()) {
            if (friend.getFriends().contains(i))
                list.add(userStorage.getUsersMap().get(i));
        }
        return list;
    }

}
