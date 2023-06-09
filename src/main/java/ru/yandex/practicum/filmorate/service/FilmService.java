package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.service.ValidationUtils.*;


@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(String id) throws NotFoundException {
        return filmStorage.getFilm(id);
    }

    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    public String addLike(String filmId, String id) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, filmStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (film == null) {
            throw new NotFoundException("Такого фильма нет");
        }
        film.getLikedId().add(user.getId());
        return String.format("Пользователь %s поставил лайк фильму %s",
                user.getName(), film.getName());
    }

    public String removeLike(String id, String filmId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, filmStorage);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (film == null) {
            throw new NotFoundException("Такого фильма нет");
        }
        film.getLikedId().remove(user.getId());
        return String.format("Пользователь %s убрал лайк фильму %s",
                user.getName(), film.getName());
    }

    public List<Film> getPopularFilms(String count) {
        int number = checkId(count);
        Map<Integer, Integer> likesFilms = new HashMap<>();
        for (Film film : filmStorage.getFilms()) {
            likesFilms.put(film.getId(), film.getLikedId().size());
        }
        List<Film> popularFilms = new ArrayList<>();
        likesFilms.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()).
                limit(number).forEach(entry -> popularFilms.add(filmStorage.getFilmsMap().get(entry.getKey())));

        return popularFilms;
    }

}
