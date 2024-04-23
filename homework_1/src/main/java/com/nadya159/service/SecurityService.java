package com.nadya159.service;

import com.nadya159.entity.User;

import java.util.Optional;

public interface SecurityService<E> {

    User register(String login, String password);

    Optional<E> authorize(String login, String password);
}
