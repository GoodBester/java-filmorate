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
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Servicing.*;


@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public String addLike(String filmId, String id) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, filmStorage);
        if (user == null || film == null) throw new NotFoundException("Обьект не найден");
        film.getLikes().add(user.getId());
        return String.format("Пользователь %s поставил лайк фильму %s",
                user.getName(), film.getName());
    }

    public String removeLike(String id, String filmId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, filmStorage);
        if (user == null || film == null) throw new NotFoundException("Обьект не найден");
        film.getLikes().remove(user.getId());
        return String.format("Пользователь %s убрал лайк фильму %s",
                user.getName(), film.getName());
    }

    public List<Film> getPopularFilms(String count) {
        int number = checkId(count);
        Map<Integer, Integer> likesFilms = new HashMap<>();
        for (Film film : filmStorage.getFilms()) likesFilms.put(film.getId(), film.getLikes().size());
        List<Integer> likes = likesFilms.values().stream().sorted(Comparator.reverseOrder()).limit(number).collect(Collectors.toList());
        List<Film> popularFilms = new ArrayList<>();
        for (Integer like : likes) {
            for (Integer num : likesFilms.keySet()) {
                if (filmStorage.getFilmsMap().get(num).getLikes().size() == like)
                    popularFilms.add(filmStorage.getFilmsMap().get(num));
            }
        }
        return popularFilms;
    }

}
