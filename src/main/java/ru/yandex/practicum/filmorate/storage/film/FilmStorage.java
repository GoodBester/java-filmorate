package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms() throws NotFoundException;

    Film getFilm(int id) throws NotFoundException;

    Film addFilm(Film film) throws ValidationException, NotFoundException;

    Film updateFilm(Film film) throws ValidationException, NotFoundException;

    List<Film> getPopularFilms(int count) throws NotFoundException;

    String addLike(int filmId, int id) throws ValidationException, NotFoundException;

    String removeLike(int id, int filmId) throws ValidationException, NotFoundException;

    List<Genre> getAllGenres() throws NotFoundException;

    List<Mpa> getAllRating() throws NotFoundException;

    Genre getGenre(int id) throws NotFoundException;

    Mpa getRating(int id) throws NotFoundException;


}
