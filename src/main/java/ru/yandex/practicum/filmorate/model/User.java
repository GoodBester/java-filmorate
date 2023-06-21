package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private String name;
    @NotNull
    private final Set<Integer> friendsId = new HashSet<>();
    @Email
    private final String email;
    @NotNull
    @NotBlank
    private final String login;
    @Past
    private final LocalDate birthday;
    private Map<Integer, String> friendStatus;

}
