package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.Servicing.checkId;
import static ru.yandex.practicum.filmorate.validators.FilmValidator.checkValid;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    public int id = 1;


    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(String id) throws NotFoundException {
        Film film = films.get(checkId(id));
        if (film == null) throw new NotFoundException("Такого фильма не существует");
        return film;
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
}
