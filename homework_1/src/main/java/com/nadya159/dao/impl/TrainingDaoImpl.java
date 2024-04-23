package com.nadya159.dao.impl;

import com.nadya159.dao.TrainingDao;
import com.nadya159.entity.Training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TrainingDaoImpl implements TrainingDao {

    private final Map<Long, Training> trainings = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<Training> findById(Long id) {
        Training training = trainings.get(id);
        return Optional.ofNullable(training);
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(trainings.values());
    }

    @Override
    public void save(Training training) {
        training.setId(id++);
        trainings.put(training.getId(), training);
    }

    @Override
    public void delete(Long id) {
        if (trainings.containsKey(id)) {
            trainings.remove(id);
        }
    }
}
