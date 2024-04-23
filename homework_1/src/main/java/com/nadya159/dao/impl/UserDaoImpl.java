package com.nadya159.dao.impl;

import com.nadya159.dao.UserDao;
import com.nadya159.entity.User;
import com.nadya159.entity.types.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    public UserDaoImpl() {
        User admin = User.builder()
                .id(1L)
                .login("admin@gmail.com")
                .password("admin")
                .role(Role.ADMIN)
                .build();
        save(admin);
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<User> user = Optional.empty();
        for (User u : users.values()) {
            if (u.getLogin().equals(login)) {
                user = Optional.of(u);
            }
        }
        return user;
    }

    @Override
    public void save(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
    }
}
