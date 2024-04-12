package com.nadya159.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс тренировок пользователей
 */
@Getter
@Builder
@AllArgsConstructor
public class Training {
    private String email;
    private LocalDate date;
    private Integer typeId;
    private LocalTime period;
    private int calories;
    private String description;
}
