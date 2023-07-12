package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.ValidationUtils;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.validators.UserValidator.checkValid;

@Component
@Qualifier("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getUsers() {
        List<User> u;
        try {
            u = jdbcTemplate.queryForObject("SELECT u.id, u.name, u.email, u.login, u.birthday, f.friend, f.STATUS FROM USER u " +
                    "left JOIN FRIENDS f ON u.ID = f.USER ORDER BY u.id;", listUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
        return u;
    }

    @Override
    public User addUser(User user) throws ValidationException {
        if (checkValid(user)) {
            insertInUser(user);
            insertInFriend(user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException, NotFoundException {
        checkValid(user);
        getUser(String.valueOf(user.getId()));
        jdbcTemplate.update("UPDATE user SET name=?, email =?, login=?, birthday=? WHERE id=?", user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday(), user.getId());
        for (Map.Entry<Integer, String> ent : user.getFriendStatus().entrySet()) {
            jdbcTemplate.update("UPDATE friends SET status =? WHERE user=? AND friend =?", ent.getValue(), user.getId(), ent.getKey());
        }

        return getUser(String.valueOf(user.getId()));
    }

    @Override
    public User getUser(String id) throws NotFoundException {
        List<User> u = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.login, u.birthday, f.friend, f.STATUS FROM USER u " +
                "left JOIN FRIENDS f ON u.ID = f.USER WHERE u.id = ?;", userRowMapper(), ValidationUtils.checkId(id));
        if (u.size() == 0) {
            throw new NotFoundException("User not found");
        }
        return u.get(0);
    }

    @Override
    public String addFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = getUser(id);
        User friend = getUser(friendId);
        if (checkValid(user) && checkValid(friend)) {
            if (user.getFriendsId().contains(friend.getId()) && friend.getFriendsId().contains(user.getId())) {
                return "You are already friends";
            } else if (user.getFriendStatus().containsKey(friend.getId()) && !friend.getFriendStatus().containsKey(user.getId())) {
                return "Request already send";
            } else if (friend.getFriendStatus().containsKey(user.getId())) {
                jdbcTemplate.update("INSERT INTO friends (user, friend, status) VALUES (?,?,?)", user.getId(), friend.getId(), "дружба");
                jdbcTemplate.update("UPDATE friends SET status=? WHERE user=? AND friend=?", "дружба", friend.getId(), user.getId());
                return "Now you are friends";
            } else {
                jdbcTemplate.update("INSERT INTO friends (user, friend, status) VALUES (?,?,?)", user.getId(), friend.getId(), "отправлено");
            }
        }
        return "Request send";
    }

    @Override
    public String deleteFriend(String id, String friendId) throws ValidationException, NotFoundException {
        User user = getUser(id);
        User friend = getUser(friendId);
        if (checkValid(user) && checkValid(friend)) {
            if (user.getFriendsId().contains(friend.getId()) && friend.getFriendsId().contains(user.getId())) {
                jdbcTemplate.update("DELETE FROM friends WHERE user=? AND friend=?", user.getId(), friend.getId());
                jdbcTemplate.update("UPDATE friends SET status=? WHERE user=? AND friend=?", "отправлено", friend.getId(), user.getId());
                return "User removed from friends";
            } else if (user.getFriendStatus().containsKey(friend.getId()) && !friend.getFriendStatus().containsKey(user.getId())) {
                jdbcTemplate.update("DELETE FROM friends WHERE user=? AND friend=?", user.getId(), friend.getId());
                return "Request removed";
            }
        }
        return "It is not your friend";
    }

    @Override
    public List<User> getFriends(String id) throws NotFoundException {
        User user = getUser(id);
        List<User> u;
        try {
            u = jdbcTemplate.queryForObject("SELECT u.id, u.name, u.email, u.login, u.birthday, f.friend," +
                    " f.STATUS FROM USER u left JOIN FRIENDS f ON u.ID = f.friend  WHERE f.user=? ORDER BY u.id;", listUserRowMapper(), user.getId());
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
        return u;
    }

    @Override
    public List<User> getCommonFriends(String id, String friendId) throws NotFoundException {
        User user = getUser(id);
        User friend = getUser(friendId);
        List<User> u;
        try {
            u = jdbcTemplate.queryForObject("SELECT u.id, u.name, u.email, u.login, u.birthday, f.friend," +
                    " f.STATUS FROM USER u left JOIN FRIENDS f ON u.ID = f.user  WHERE u.ID IN(SELECT r.friend fr FROM " +
                    "(SELECT f.friend rrr, f2.friend FROM friends f LEFT JOIN friends f2 ON f.friend = f2.FRIEND WHERE f.USER " +
                    "= ? AND f2.USER = ? AND f.FRIEND = f2.FRIEND)  AS r) " +
                    "ORDER BY u.id;", listUserRowMapper(), user.getId(), friend.getId());
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
        return u;
    }

    private RowMapper<List<User>> listUserRowMapper() {
        return (rs, rowNum) -> {
            HashMap<Integer, User> users = new HashMap<>();
            do {
                String s;
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                if (!users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                }
                s = rs.getString("status");
                if (s == null) continue;
                if (s.equals("дружба")) {
                    users.get(user.getId()).getFriendsId().add(rs.getInt("friend"));
                }
                users.get(user.getId()).getFriendStatus().put(rs.getInt("friend"), rs.getString("status"));
            } while (rs.next());
            return new ArrayList<>(users.values());
        };
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            String s;
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            do {
                s = rs.getString("status");
                if (s == null) continue;
                if (s.equals("дружба")) {
                    user.getFriendsId().add(rs.getInt("friend"));
                }
                user.getFriendStatus().put(rs.getInt("friend"), rs.getString("status"));
            } while (rs.next());
            return user;
        };
    }

    private User insertInUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of("name", user.getName(), "email", user.getEmail(), "login",
                user.getLogin(), "birthday", user.getBirthday().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        return user;
    }

    private User insertInFriend(User user) {
        for (Map.Entry<Integer, String> ent : user.getFriendStatus().entrySet()) {
            jdbcTemplate.update("INSERT INTO friends (user, friend, status) VALUES (?,?,?)", user.getId(), ent.getKey(), ent.getValue());
        }
        return user;
    }

}

