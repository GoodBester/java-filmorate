package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import static ru.yandex.practicum.filmorate.validators.UserValidator.checkValid;

public class ValidationUtils {

    public static User checkUser(String idStr, UserStorage userStorage) throws ValidationException, NotFoundException {
        User user;
        int id = checkId(idStr);
        if (!userStorage.getUsers().contains(userStorage.getUser(idStr))) {
            return null;
        }
        user = userStorage.getUser(idStr);
        if (checkValid(user)) {
            return user;
        }
        return null;
    }

    public static Film checkFilm(String idStr, FilmStorage filmStorage) throws ValidationException, NotFoundException {
        Film film;
        int id = checkId(idStr);
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilm(idStr))) {
            return null;
        }
        film = filmStorage.getFilm(idStr);
        if (FilmValidator.checkValid(film)) {
            return film;
        }
        return null;
    }

    public static int checkId(String id) throws NotFoundException {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (Throwable e) {
            throw new NotFoundException("Некоректное значение параметра");
        }
        return idInt;
    }
}
