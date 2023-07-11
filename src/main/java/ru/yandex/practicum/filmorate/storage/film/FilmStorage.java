package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    List<Film> getAllFilms() throws NotFoundException;

    Film getFilm(String id) throws NotFoundException;

    Film addFilm(Film film) throws ValidationException, NotFoundException;

    Film updateFilm(Film film) throws ValidationException, NotFoundException;

    Map<Integer, Film> getFilmsMap();

    List<Film> getPopularFilms(String count) throws NotFoundException;

    String addLike(String filmId, String id) throws ValidationException, NotFoundException;

    String removeLike(String id, String filmId) throws ValidationException, NotFoundException;

    List<Genre> getAllGenres() throws NotFoundException;

    List<Mpa> getAllRating() throws NotFoundException;

    Genre getGenre(String id) throws NotFoundException;

    Mpa getRating(String id) throws NotFoundException;


}
