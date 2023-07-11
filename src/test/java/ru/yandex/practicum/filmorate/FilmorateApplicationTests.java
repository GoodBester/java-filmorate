package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    @Test
    public void testGetUser() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(String.valueOf(1)));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1).hasFieldOrPropertyWithValue("login", "Login")
                );

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userStorage.getUser("999999"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetUsers() throws ValidationException {
        assertThat(userStorage.getUsers()).asList().size().isEqualTo(2);
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login11");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        assertThat(userStorage.getUsers()).asList().isNotEmpty().contains(user1).size().isEqualTo(3);
        User user2 = new User();
        user2.setName("name");
        user2.setLogin("23235");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);
        assertThat(userStorage.getUsers()).asList().isNotEmpty().contains(user1, user2).size().isEqualTo(4);
    }

    @Test
    public void testAddUser() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        assertThat(userStorage.getUsers()).asList().isNotEmpty().contains(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(String.valueOf(1)));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1).hasFieldOrPropertyWithValue("login", "Login")
                );
    }

    @Test
    public void testUpdateUser() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Log2121in");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        assertThat(userStorage.getUsers()).asList().isNotEmpty().contains(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(String.valueOf(1)));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1).hasFieldOrPropertyWithValue("login", "Login")
                );
        User user2 = new User();
        user2.setId(10);
        user2.setName("na3243me");
        user2.setLogin("Login66662");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.updateUser(user2);
        assertThat(userStorage.getUsers()).asList().isNotEmpty().contains(user2);
        Optional<User> userOptional2 = Optional.ofNullable(userStorage.getUser(String.valueOf(10)));
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 10).hasFieldOrPropertyWithValue("login", "Login66662")
                );
    }

    @Test
    public void testAddFriend() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        User user2 = new User();
        user2.setName("name");
        user2.setLogin("Login2");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);
        assertThat(userStorage.getUser("1").getFriendsId().isEmpty());
        userStorage.addFriend("1", "2");
        assertThat(userStorage.getUser("1").getFriendsId().contains(2));
    }

    @Test
    public void testDeleteFriend() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        User user2 = new User();
        user2.setName("name");
        user2.setLogin("Login2");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);
        assertThat(userStorage.getUser("1").getFriendsId().isEmpty());
        userStorage.addFriend("1", "2");
        assertThat(userStorage.getUser("1").getFriendsId().contains(2));
        userStorage.deleteFriend("1", "2");
        assertThat(userStorage.getUser("1").getFriendsId().isEmpty());
    }

    @Test
    public void testGetFriends() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        User user2 = new User();
        user2.setName("name");
        user2.setLogin("Login2");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);
        assertThat(userStorage.getUser("1").getFriendsId().isEmpty());
        userStorage.addFriend("1", "2");
        assertThat(userStorage.getUser("1").getFriendsId().contains(2));
        assertThat(userStorage.getFriends("1").contains(user2));
    }

    @Test
    public void testGetCommonFriends() throws NotFoundException, ValidationException {
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        User user2 = new User();
        user2.setName("name");
        user2.setLogin("Login2");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);
        assertThat(userStorage.getUser("1").getFriendsId().isEmpty());
        userStorage.addFriend("1", "2");
        assertThat(userStorage.getUser("1").getFriendsId().contains(2));
        assertThat(userStorage.getFriends("1").contains(user2));
        User user3 = new User();
        user3.setName("name");
        user3.setLogin("Login3");
        user3.setEmail("email@");
        user3.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user3);
        userStorage.addFriend("3", "2");
        assertThat(userStorage.getCommonFriends("1", "3").contains(user2));
    }

    @Test
    public void testGetFilm() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "PG"));
        filmStorage.addFilm(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(String.valueOf(1)));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1).hasFieldOrPropertyWithValue("name", "name")
                );

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.getFilm("999999"));
        assertEquals("Фильм не найден", exception.getMessage());
    }

    @Test
    public void testGetAllFilms() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film);
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(2000, 2, 1));
        film1.setDuration(100);
        film1.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film1);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film1);
    }

    @Test
    public void testAddFilm() throws NotFoundException, ValidationException {
        assertThat(filmStorage.getAllFilms()).asList().size().isEqualTo(3);
        Film film = new Film();
        film.setName("name");
        film.setDescription("aefafaw");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film).size().isEqualTo(4);
    }

    @Test
    public void testUpdateFilm() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("desc2222ription");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film);
        Film film1 = new Film();
        film1.setId(8);
        film1.setName("name2");
        film1.setDescription("descr2r4214iption2");
        film1.setReleaseDate(LocalDate.of(2000, 2, 1));
        film1.setDuration(100);
        film1.setMpa(new Mpa(1, "G"));
        filmStorage.updateFilm(film1);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film1).doesNotContain(film);
    }

    @Test
    public void testAddLike() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film);
        assertThat(filmStorage.getFilm("1").getLikes()).isEqualTo(2);
        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);
        filmStorage.addLike("1", "3");
        assertThat(filmStorage.getFilm("1").getLikes()).isEqualTo(3);
    }

    @Test
    public void testRemoveLike() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);
        assertThat(filmStorage.getAllFilms()).asList().isNotEmpty().contains(film);
        assertThat(filmStorage.getFilm("8").getLikes()).isEqualTo(0);
        filmStorage.addLike("8", "1");
        assertThat(filmStorage.getFilm("8").getLikes()).isEqualTo(1);
        filmStorage.removeLike("1", "8");
        assertThat(filmStorage.getFilm("8").getLikes()).isEqualTo(0);
    }

    @Test
    public void testGetPopularFilms() throws NotFoundException, ValidationException {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 1));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film);

        Film film1 = new Film();
        film1.setName("name1");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(2000, 2, 1));
        film1.setDuration(100);
        film1.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film1);

        Film film2 = new Film();
        film2.setName("name2");
        film2.setDescription("description");
        film2.setReleaseDate(LocalDate.of(2000, 2, 1));
        film2.setDuration(100);
        film2.setMpa(new Mpa(1, "G"));
        filmStorage.addFilm(film2);

        User user1 = new User();
        user1.setName("name");
        user1.setLogin("Login");
        user1.setEmail("email@");
        user1.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user1);

        User user2 = new User();
        user2.setName("name");
        user2.setLogin("Login");
        user2.setEmail("email@");
        user2.setBirthday(LocalDate.of(2000, 12, 27));
        userStorage.addUser(user2);

        filmStorage.addLike("1", "1");
        filmStorage.addLike("1", "2");
        filmStorage.addLike("3", "1");
        film.setLikes(2);
        film2.setLikes(1);
        assertThat(filmStorage.getPopularFilms("10")).asList().isNotEmpty().containsExactly(film, film2, film1);
    }

    @Test
    public void testGetGenre() throws NotFoundException {
        Genre genre = new Genre(1, "Комедия");
        assertThat(filmStorage.getGenre("1")).isEqualTo(genre);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.getGenre("999999"));
        assertEquals("Жанр не найден", exception.getMessage());
    }

    @Test
    public void testGetAllGenre() throws NotFoundException {
        assertThat(filmStorage.getAllGenres()).asList().isNotEmpty().size().isSameAs(6);
    }

    @Test
    public void testGetRating() throws NotFoundException {
        Mpa mpa = new Mpa(1, "G");
        assertThat(filmStorage.getRating("1")).isEqualTo(mpa);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmStorage.getRating("999999"));
        assertEquals("Рейтинг не найден", exception.getMessage());
    }

    @Test
    public void testGetAllRating() throws NotFoundException {
        assertThat(filmStorage.getAllRating()).asList().isNotEmpty().size().isSameAs(5);
    }

}