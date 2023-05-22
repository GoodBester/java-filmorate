package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@Slf4j
public abstract class Validator {

    protected void ex(String message) throws ValidationException {
        log.warn("Получена ошибка с сообщением: {}" ,message);
        throw new ValidationException(message);
    }

}
