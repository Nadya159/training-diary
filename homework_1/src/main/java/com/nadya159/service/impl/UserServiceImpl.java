package com.nadya159.service.impl;

import com.nadya159.dao.UserDao;
import com.nadya159.entity.User;
import com.nadya159.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Класс, реализующий бизнес-логику приложения по работе с пользователями
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }
}
