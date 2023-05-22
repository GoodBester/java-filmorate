package ru.yandex.practicum.filmorate.validatorsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
        //Полность. корректный фильм
        Film film = Film.builder().name("qqq").description("qqq").duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(film));
        //Граница описания
        String s = "";
        for (int i = 0; i < 200; i++) s += "q";
        Film film1 = Film.builder().name("qqq").description(s).duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(film1));
        Film film2 = Film.builder().name("qqq").description("").duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(film2));
        //Граница продолжительности
        Film film3 = Film.builder().name("qqq").description("").duration(0).releaseDate(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(film3));
        //Граница даты
        Film film4 = Film.builder().name("null").description("s").duration(9).releaseDate(LocalDate.of(1895, 12, 28)).build();
        assertTrue(validator.checkValid(film4));
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmName() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name(null).description("qqq").duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по имени", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name("  ").description("qqq").duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
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
                    Film film = Film.builder().name("null").description(s).duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по описанию", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    String s = "";
                    for (int i = 0; i < 900; i++) s += "q";
                    Film film = Film.builder().name("null").description(s).duration(10).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по описанию", exception1.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name("null").description("s").duration(-1).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по продолжительности", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name("null").description("s").duration(-1111).releaseDate(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по продолжительности", exception1.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectFilmReleaseDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name("null").description("s").duration(9).releaseDate(LocalDate.of(1895, 12, 27)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по Дате выпуска", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = Film.builder().name("null").description("s").duration(99).releaseDate(LocalDate.of(1700, 12, 28)).build();
                    validator.checkValid(film);
                });
        assertEquals("Валидация не пройдена по Дате выпуска", exception1.getMessage());
    }


}