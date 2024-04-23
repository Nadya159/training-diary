package com.nadya159.entity;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс тренировок пользователя
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Training {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer typeId;
    private LocalTime period;
    private int calories;
    private String description;
}
