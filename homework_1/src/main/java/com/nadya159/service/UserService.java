package com.nadya159.service;

import com.nadya159.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);
}
