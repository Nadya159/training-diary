package com.nadya159.entity;

import com.nadya159.entity.types.Role;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Класс пользователей
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String login;
    private String password;
    private Role role;
}
