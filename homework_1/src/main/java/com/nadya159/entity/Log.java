package com.nadya159.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Класс для логирования действий пользователей
 */
@Getter
@Builder
@AllArgsConstructor
public class Log {
    private String action;
    private LocalDateTime dateTime;
    private String email;
    private String actionResult;
}
