package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator extends Validator {

    public static boolean checkValid(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) ex("Валидация не пройдена по имени");
        if (film.getDescription().length() > 200) ex("Валидация не пройдена по описанию");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) ex("Валидация не пройдена по Дате выпуска");
        if (film.getDuration() < 0) ex("Валидация не пройдена по продолжительности");
        return true;
    }
}
