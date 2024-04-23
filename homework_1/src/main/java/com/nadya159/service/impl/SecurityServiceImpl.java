package com.nadya159.service.impl;

import com.nadya159.dao.UserDao;
import com.nadya159.entity.User;
import com.nadya159.entity.types.ActionResult;
import com.nadya159.entity.types.ActionType;
import com.nadya159.entity.types.Role;
import com.nadya159.exception.SecurityException;
import com.nadya159.service.AuditService;
import com.nadya159.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService<User> {

    private final UserDao userDao;
    private final AuditService auditService;

    @Override
    public User register(String login, String password) {
        if (login.isBlank() || password.isBlank()) {
            auditService.save(login, ActionType.REGISTRATION, ActionResult.FAIL);
            throw new SecurityException("Пароль или логин не могут быть пустыми или состоять только из пробелов");
        }
        if (password.length() < 3 || password.length() > 20) { //todo добавить валидацию email регуляркой
            auditService.save(login, ActionType.REGISTRATION, ActionResult.FAIL);
            throw new SecurityException("Длина пароля должна составлять от 3 до 20 символов");
        }
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isPresent()) {
            auditService.save(login, ActionType.REGISTRATION, ActionResult.FAIL);
            throw new SecurityException("Пользователь с таким логином уже существует");
        }
        User newUser = User.builder()
                .login(login)
                .password(password)
                .role(Role.USER)
                .build();
        userDao.save(newUser);
        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new SecurityException("Ошибка регистрации!"));  //todo проверить, что с ID
        auditService.save(login, ActionType.REGISTRATION, ActionResult.SUCCESS);
        return user;
    }

    @Override
    public Optional<User> authorize(String login, String password) {
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isEmpty()) {
            auditService.save(login, ActionType.AUTHORIZATION, ActionResult.FAIL);
            throw new SecurityException("Пользователь с данным логином отсутствует в базе данных");
        }
        if (!optionalUser.get().getPassword().equals(password)) {
            auditService.save(login,ActionType.AUTHORIZATION, ActionResult.FAIL);
            throw new SecurityException("Неверный пароль");
        }
        auditService.save(login, ActionType.AUTHORIZATION, ActionResult.SUCCESS);
        return optionalUser;
    }
}
