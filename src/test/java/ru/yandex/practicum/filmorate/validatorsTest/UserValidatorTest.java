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
        User user = new User();
        user.setEmail("qqq@");
        user.setLogin("qqq");
        user.setName("10");
        user.setBirthday(LocalDate.of(2000, 10, 20));
        assertTrue(UserValidator.checkValid(user));
        //Граница дня рождения
        User user1 = new User();
        user1.setEmail("qqq@");
        user1.setLogin("qqq");
        user1.setName("10");
        user1.setBirthday(LocalDate.now());
        assertTrue(UserValidator.checkValid(user1));
    }

    @Test
    public void shouldReplaceNameWithLoginIfNameIsEmpty() throws ValidationException {
        User user = new User();
        user.setEmail("qqq@");
        user.setLogin("qqq");
        user.setName(null);
        user.setBirthday(LocalDate.of(2000, 10, 20));
        assertTrue(UserValidator.checkValid(user));
        assertEquals(user.getLogin(), user.getName());
        User user1 = new User();
        user1.setEmail("qqq@");
        user1.setLogin("qqq");
        user1.setName(" ");
        user1.setBirthday(LocalDate.of(2000, 10, 20));
        assertTrue(UserValidator.checkValid(user1));
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail(null);
                    user.setLogin("qqq");
                    user.setName("10");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("  ");
                    user.setLogin("qqq");
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception1.getMessage());
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq");
                    user.setLogin("qqq");
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по имейлу", exception2.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq@");
                    user.setLogin(null);
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq@");
                    user.setLogin("  ");
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception1.getMessage());
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq@");
                    user.setLogin("iii ii");
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2000, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по логину", exception2.getMessage());
    }

    @Test
    public void shouldReturnExceptionWithIncorrectUserBirthDay() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq@");
                    user.setLogin("null");
                    user.setName("null");
                    user.setBirthday(LocalDate.of(2026, 10, 20));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по дню рождения", exception.getMessage());
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User();
                    user.setEmail("qqq@");
                    user.setLogin("null");
                    user.setName("null");
                    user.setBirthday(LocalDate.now().plusDays(1));
                    UserValidator.checkValid(user);
                });
        assertEquals("Валидация не пройдена по дню рождения", exception1.getMessage());
    }
}