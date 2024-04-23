package com.nadya159.service;

import com.nadya159.entity.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

    List<Training> findByUserId(Long userId);

    Optional<Training> findById(Long id);

    List<Training> findAll();

    void create(Long userId, LocalDate date, Integer typeId, Integer period, Integer calories, String description);

    void update(Long userId, Long id, Integer period, Integer calories, String description);

    List<Training> getTrainingsByMonthAndYear(Integer year, Integer month, Long userId);

    List<Training> getTrainingHistory(Long userId);

    void delete(Long id);
}
