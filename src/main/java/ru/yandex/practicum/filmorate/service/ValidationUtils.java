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

    public static User checkUser(int id, UserStorage userStorage) throws ValidationException, NotFoundException {
        User user;
        if (!userStorage.getUsers().contains(userStorage.getUser(id))) {
            return null;
        }
        user = userStorage.getUser(id);
        if (checkValid(user)) {
            return user;
        }
        return null;
    }

    public static Film checkFilm(int id, FilmStorage filmStorage) throws ValidationException, NotFoundException {
        Film film;
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilm(id))) {
            return null;
        }
        film = filmStorage.getFilm(id);
        if (FilmValidator.checkValid(film)) {
            return film;
        }
        return null;
    }
}
