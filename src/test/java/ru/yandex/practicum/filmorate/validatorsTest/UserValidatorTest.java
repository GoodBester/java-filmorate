package ru.yandex.practicum.filmorate.validatorsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    static UserValidator validator;

    @BeforeAll
    public static void before() {
        validator = new UserValidator();
    }

    @Test
    public void shouldReturnTrueWithCorrectUser() throws ValidationException {
        //Полность. корректный юзер
        User user = User.builder().email("qqq@").login("qqq").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(user));
        //Граница дня рождения
        User user1 = User.builder().email("qqq@").login("qqq").name("10").birthday(LocalDate.now()).build();
        assertTrue(validator.checkValid(user1));
    }

    @Test
    public void shouldReplaceNameWithLoginIfNameIsEmpty() throws ValidationException {
        User user = User.builder().email("qqq@").login("qqq").name(null).birthday(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(user));
        assertEquals(user.getLogin(), user.getName());
        User user1 = User.builder().email("qqq@").login("qqq").name(" ").birthday(LocalDate.of(2000, 10, 20)).build();
        assertTrue(validator.checkValid(user1));
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email(null).login("qqq").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("  ").login("qqq").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception1.getMessage());
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq").login("qqq").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception2.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq@").login(null).name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq@").login("  ").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception1.getMessage());
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq@").login("iii ii").name("10").birthday(LocalDate.of(2000, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception2.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserBirthDay() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq@").login("iii").name("10").birthday(LocalDate.of(2026, 10, 20)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по дню рождения", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = User.builder().email("qqq@").login("iii").name("10").birthday(LocalDate.now().plusDays(1)).build();
                    validator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по дню рождения", exception1.getMessage());
    }
}