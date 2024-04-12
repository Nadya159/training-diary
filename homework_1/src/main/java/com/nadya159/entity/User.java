package com.nadya159.entity;

import lombok.*;

/**
 * Класс пользователей
 */
@Getter
@Builder
@AllArgsConstructor
public class User {
    private String name;
    private String email;
    private String password;
    private Role role;
}
