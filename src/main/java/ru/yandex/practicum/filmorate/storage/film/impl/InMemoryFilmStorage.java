package ru.yandex.practicum.filmorate.storage.film.impl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.service.ValidationUtils.*;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.checkValid;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final Map<Integer, Film> films = new HashMap<>();
    public int id = 1;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(String id) throws NotFoundException {
        return Optional.ofNullable(films.get(checkId(id))).orElseThrow(() -> new NotFoundException("Такого фильма нет"));
    }


    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (checkValid(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм");
        }
        return film;
    }

    @Override
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (checkValid(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм обновлен");
                return film;
            }
        }
        throw new ValidationException("Такого фильма нет в базе");
    }

    @Override
    public Map<Integer, Film> getFilmsMap() {
        return films;
    }

    public String addLike(String filmId, String id) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, this);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (film == null) {
            throw new NotFoundException("Такого фильма нет");
        }
//        film.getLikedId().add(user.getId());
        return String.format("Пользователь %s поставил лайк фильму %s",
                user.getName(), film.getName());
    }

    public String removeLike(String id, String filmId) throws ValidationException, NotFoundException {
        User user = checkUser(id, userStorage);
        Film film = checkFilm(filmId, this);
        if (user == null) {
            throw new NotFoundException("Такого юзера нет");
        }
        if (film == null) {
            throw new NotFoundException("Такого фильма нет");
        }
//        film.getLikedId().remove(user.getId());
        return String.format("Пользователь %s убрал лайк фильму %s",
                user.getName(), film.getName());
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public List<Mpa> getAllRating() {
        return null;
    }

    @Override
    public Genre getGenre(String id) {
        return null;
    }

    @Override
    public Mpa getRating(String id) {
        return null;
    }

    @Override
    public List<Film> getPopularFilms(String count) {
        int number = checkId(count);
        Map<Integer, Integer> likesFilms = new HashMap<>();
//        for (Film film : films.values()) {
//            likesFilms.put(film.getId(), film.getLikedId().size());
//        }
        List<Film> popularFilms = new ArrayList<>();
        likesFilms.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(number).forEach(entry -> popularFilms.add(this.getFilms().get(entry.getKey())));

        return popularFilms;
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }
}
