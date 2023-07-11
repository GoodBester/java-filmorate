package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public List<Film> getAllFilms() throws NotFoundException {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable String id) throws NotFoundException {
        return filmService.getFilm(id);
    }


    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) throws ValidationException, NotFoundException {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws ValidationException, NotFoundException {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String addLike(@PathVariable String id, @PathVariable String userId) throws ValidationException, NotFoundException {
        return filmService.addLike(id, userId);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public String removeLike(@PathVariable String id, @PathVariable String userId) throws ValidationException, NotFoundException {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) throws NotFoundException {
        return filmService.getPopularFilms(count);
    }
}
