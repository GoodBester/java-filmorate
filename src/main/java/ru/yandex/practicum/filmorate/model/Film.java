package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    private final Set<Integer> likes = new HashSet<>();
    private String description;
    private final LocalDate releaseDate;
    @Min(0)
    private final int duration;
}
