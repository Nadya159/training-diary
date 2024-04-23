package com.nadya159.dao;

import com.nadya159.entity.User;

import java.util.Optional;

public interface UserDao extends BaseDao<Long, User> {

    Optional<User> findByLogin(String login);
}
