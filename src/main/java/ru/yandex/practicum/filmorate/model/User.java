package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private int id;
    private String name;
    private Set<Integer> friendsId = new HashSet<>();
    private String email;
    private String login;
    private LocalDate birthday;
    private Map<Integer, String> friendStatus = new HashMap<>();
}
