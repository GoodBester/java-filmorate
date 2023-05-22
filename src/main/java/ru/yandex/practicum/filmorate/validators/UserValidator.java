package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class UserValidator extends Validator {

    public boolean checkValid(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@")))
            ex("Валидация не пройдена по имейлу");
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" "))
            ex("Валидация не пройдена по логину");
        if (user.getBirthday().isAfter(LocalDate.now())) ex("Валидация не пройдена по дню рождения");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return true;
    }
}
