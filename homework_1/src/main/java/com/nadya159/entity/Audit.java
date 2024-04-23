package com.nadya159.entity;

import com.nadya159.entity.types.ActionResult;
import com.nadya159.entity.types.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс для логирования действий пользователей
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Audit {
    private Long id;
    private String login;
    private ActionType action;
    private LocalDateTime dateTime;
    private ActionResult actionResult;
}
