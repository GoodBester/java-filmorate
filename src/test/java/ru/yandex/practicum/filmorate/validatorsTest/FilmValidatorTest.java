package ru.yandex.practicum.filmorate.validatorsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    static FilmValidator validator;

    @BeforeAll
    public static void before() {
        validator = new FilmValidator();
    }

    @Test
    public void shouldReturnTrueWithCorrectFilm() throws ValidationException {
//        Полность. корректный фильм
        Film film = new Film();
        film.setName("qqq");
        film.setDescription("qqq");
        film.setDuration(10);
        film.setReleaseDate(LocalDate.of(2000, 10, 20));
        assertTrue(FilmValidator.checkValid(film));
        //Граница описания
        String s = "";
        for (int i = 0; i < 200; i++) s += "q";
        Film film1 = new Film();
        film1.setName("qqq");
        film1.setDescription(s);
        film1.setDuration(10);
        film1.setReleaseDate(LocalDate.of(2000, 10, 20));
        assertTrue(FilmValidator.checkValid(film1));
        Film film2 = new Film();
        film2.setName("qqq");
        film2.setDescription(" ");
        film2.setDuration(10);
        film2.setReleaseDate(LocalDate.of(2000, 10, 20));
        assertTrue(FilmValidator.checkValid(film2));
        //Граница продолжительности
        Film film3 = new Film();
        film3.setName("qqq");
        film3.setDescription(" ");
        film3.setDuration(0);
        film3.setReleaseDate(LocalDate.of(2000, 10, 20));
        assertTrue(FilmValidator.checkValid(film3));
        //Граница даты
        Film film4 = new Film();
        film4.setName("qqq");
        film4.setDescription("qqq");
        film4.setDuration(10);
        film4.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertTrue(FilmValidator.checkValid(film4));
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmName() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName(null);
                    film.setDescription("qqq");
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по имени", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName("  ");
                    film.setDescription("qqq");
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по имени", exception1.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    String s = "";
                    for (int i = 0; i < 201; i++) s += "q";
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription(s);
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по описанию", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    String s = "";
                    for (int i = 0; i < 900; i++) s += "q";
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription(s);
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по описанию", exception1.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription("qqq");
                    film.setDuration(-1);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по продолжительности", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription("qqq");
                    film.setDuration(-1111);
                    film.setReleaseDate(LocalDate.of(2000, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по продолжительности", exception1.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmReleaseDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription("qqq");
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(1895, 12, 27));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по Дате выпуска", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film();
                    film.setName("qqq");
                    film.setDescription("qqq");
                    film.setDuration(10);
                    film.setReleaseDate(LocalDate.of(1700, 10, 20));
                    FilmValidator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по Дате выпуска", exception1.getMessage());
    }
}