package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class Film{
    private int id;
    @NotNull
    @NotBlank
    private final String name;

    private String description;
    private final LocalDate releaseDate;
    @Min(0)
    private final int duration;
}
