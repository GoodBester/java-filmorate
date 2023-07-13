package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;


@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() throws NotFoundException {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) throws NotFoundException {
        return filmStorage.getFilm(id);
    }

    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        return filmStorage.updateFilm(film);
    }

    public String addLike(int filmId, int id) throws ValidationException, NotFoundException {
        return filmStorage.addLike(filmId, id);
    }

    public String removeLike(int id, int filmId) throws ValidationException, NotFoundException {
        return filmStorage.removeLike(id, filmId);
    }

    public List<Film> getPopularFilms(int count) throws NotFoundException {
        return filmStorage.getPopularFilms(count);
    }

    public List<Mpa> getAllRating() throws NotFoundException {
        return filmStorage.getAllRating();
    }

    public List<Genre> getAllGenres() throws NotFoundException {
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(int id) throws NotFoundException {
        return filmStorage.getGenre(id);
    }

    public Mpa getRating(int id) throws NotFoundException {
        return filmStorage.getRating(id);
    }
}
