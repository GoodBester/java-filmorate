package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validators.FilmValidator.checkValid;

@Component
@Slf4j
@AllArgsConstructor
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private UserService userService;

    @Autowired
    public FilmDbStorage(UserService userService, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Film> getAllFilms() {
        String select = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id rating_id," +
                " r.NAME rating_name, COUNT(fl.USER_ID) likes, g.NAME genre_name, g.id genre_id FROM FILM as f ";
        String joinRating = "LEFT JOIN RATING r ON f.RATING_ID = r.ID LEFT JOIN FILM_LIKES fl ON f.ID = fl.FILM_ID" ;
        String joinFilmGenre = " LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID left JOIN GENRE g ON fg.GENRE_ID = g.ID ";
        String condition = "GROUP BY f.id, g.NAME, g.id ORDER BY f.ID";
        List<Film> f;
        try {
            f = jdbcTemplate.queryForObject(select + joinRating + joinFilmGenre + condition, allFilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
        return f;
    }


    @Override
    public Film getFilm(int id) throws NotFoundException {
        String select = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.RATING_ID rating_id, r.NAME rating_name," +
                " COUNT(fl.USER_ID) likes, g.NAME genre_name, g.id genre_id FROM FILM as f ";
        String joinRating = "LEFT JOIN RATING r ON f.RATING_ID = r.ID LEFT JOIN FILM_LIKES fl ON f.ID = fl.FILM_ID ";
        String joinFilmGenre = "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID left JOIN GENRE g ON fg.GENRE_ID = g.ID ";
        String condition = "WHERE f.id = ? GROUP BY f.id, g.NAME ORDER BY genre_id;";
        Film f;
        try {
            f = jdbcTemplate.queryForObject(select + joinRating + joinFilmGenre + condition, filmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Такого фильма нет");
        }
        return f;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        checkValid(film);
        insertInFilm(film);
        log.info("Добавлен новый фильм");
        insertInFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        checkValid(film);
        jdbcTemplate.update("UPDATE film SET name=?, description=?, release_date=?, duration=?, rating_id=? WHERE id=?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id=?", film.getId());
        log.info("Фильм обновлён");
        film.setGenres(film.getGenres().stream().sorted().collect(Collectors.toList()));
        return insertInFilmGenre(film);
    }


    @Override
    public List<Film> getPopularFilms(int count) {
        String select = "SELECT f.id, f.name, f.description, f.release_date, f.duration, r.ID rating_id, r.NAME rating_name," +
                " COUNT(fl.USER_ID) likes, g.NAME genre_name, g.id genre_id FROM FILM as f ";
        String joinRating = "LEFT JOIN RATING r ON f.RATING_ID = r.ID LEFT JOIN FILM_LIKES fl ON f.ID = fl.FILM_ID ";
        String joinFilmGenre = "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID left JOIN GENRE g ON fg.GENRE_ID = g.ID ";
        String condition = "GROUP BY f.id, g.NAME, g.id ORDER BY likes DESC LIMIT ?;";
        List<Film> f;
        try {
            f = jdbcTemplate.queryForObject(select + joinRating + joinFilmGenre + condition, allFilmRowMapper(), count);
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return List.of();
        }
        return f.stream().sorted(Comparator.comparingInt(Film::getLikes).reversed()).collect(Collectors.toList());
    }

    @Override
    public String addLike(int filmId, int id) throws NotFoundException, ValidationException {
        getFilm(filmId);
        userService.getUser(id);
        jdbcTemplate.update("INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?,?);", filmId, id);
        return "Like added";
    }

    @Override
    public String removeLike(int id, int filmId) throws ValidationException, NotFoundException {
        userService.getUser(id);
        getFilm(filmId);
        jdbcTemplate.update("DELETE FROM FILM_LIKES WHERE film_id=? AND user_id=?;", filmId, id);
        return "Like removed";
    }


    private Film insertInFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("film")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("name", film.getName(), "description", film.getDescription(), "release_date",
                film.getReleaseDate().toString(), "duration", String.valueOf(film.getDuration()), "rating_id", String.valueOf(film.getMpa().getId()));
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());
        return film;
    }

    private Film insertInFilmGenre(Film film) throws NotFoundException {
        for (Integer g : film.getGenres().stream().map(Genre::getId).distinct().collect(Collectors.toList())) {
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?,?);", film.getId(), g);
        }
        return getFilm(film.getId());
    }


    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setDuration(rs.getInt("duration"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setLikes(rs.getInt("likes"));
            film.setMpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")));
            do {
                if (rs.getInt("genre_id") != 0) {
                    film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                }
            } while (rs.next());
            return film;
        };
    }

    private RowMapper<List<Film>> allFilmRowMapper() {
        return (rs, rowNum) -> {
            Map<Integer, Film> films = new HashMap<>();
            do {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setDuration(rs.getInt("duration"));
                film.setLikes(rs.getInt("likes"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setMpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")));
                if (films.containsKey(film.getId())) {
                    if (rs.getInt("genre_id") != 0) {
                        films.get(film.getId()).getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                    }
                } else {
                    if (rs.getInt("genre_id") != 0) {
                        film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                    }
                    films.put(film.getId(), film);
                }
            } while (rs.next());
            return new ArrayList<>(films.values());
        };
    }

    @Override
    public List<Genre> getAllGenres() throws NotFoundException {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre", ((rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"))));
        if (genres.size() == 0) {
            throw new NotFoundException("Жанры не найдены");
        }
        return genres;
    }

    @Override
    public List<Mpa> getAllRating() throws NotFoundException {
        List<Mpa> mpas = jdbcTemplate.query("SELECT * FROM rating", ((rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name"))));
        if (mpas.size() == 0) {
            throw new NotFoundException("Рейтинги не найдены");
        }
        return mpas;
    }

    @Override
    public Genre getGenre(int id) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genre WHERE id =?", ((rs, rowNum) ->
                    new Genre(rs.getInt("id"), rs.getString("name"))), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр не найден");
        }
    }

    @Override
    public Mpa getRating(int id) throws NotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM rating WHERE id =?", ((rs, rowNum) ->
                    new Mpa(rs.getInt("id"), rs.getString("name"))), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг не найден");
        }
    }
}
