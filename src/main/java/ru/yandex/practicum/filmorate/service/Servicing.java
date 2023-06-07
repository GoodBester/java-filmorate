package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import static ru.yandex.practicum.filmorate.validators.UserValidator.checkValid;

public class Servicing {

    public static User checkUser(String idStr, UserStorage userStorage) throws ValidationException {
        User user;
        int id = checkId(idStr);
        if (!userStorage.getUsersMap().containsKey(id)) return null;
        user = userStorage.getUsersMap().get(id);
        if (checkValid(user)) return user;
        return null;
    }

    public static Film checkFilm(String idStr, FilmStorage filmStorage) throws ValidationException {
        Film film;
        int id = checkId(idStr);
        if (!filmStorage.getFilmsMap().containsKey(id)) return null;
        film = filmStorage.getFilmsMap().get(id);
        if (FilmValidator.checkValid(film)) return film;
        return null;
    }

    public static int checkId(String id) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (Throwable e) {
            return -1;
        }
        return idInt;
    }
}
