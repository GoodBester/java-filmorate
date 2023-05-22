package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films;
    private final FilmValidator validator;
    public int id;

    public FilmController() {
        this.validator = new FilmValidator();
        this.films = new HashMap<>();
        this.id = 1;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (validator.checkValid(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException {
        if (validator.checkValid(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм обновлен");
                return film;
            }
        }
        throw new ValidationException("Такого фильма нет в базе");
    }
}
