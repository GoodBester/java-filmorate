package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film implements Comparable<Film> {
    private int id;
    private String name;
    private int likes;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa;

    @Override
    public int compareTo(Film o) {
        return this.getLikes() - o.getLikes();
    }
}
