package com.nadya159.dao.impl;

import com.nadya159.dao.TrainingTypeDao;
import com.nadya159.entity.TrainingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TrainingTypeDaoImpl implements TrainingTypeDao {
    private final Map<Integer, TrainingType> trainingTypes = new HashMap<>();
    private Integer id = 1;

    public TrainingTypeDaoImpl() {
        save(TrainingType.builder().typeName("Yoga").build());
        save(TrainingType.builder().typeName("Cardio").build());
        save(TrainingType.builder().typeName("Power").build());
    }

    @Override
    public Optional<TrainingType> findById(Integer id) {
        return Optional.ofNullable(trainingTypes.get(id));
    }

    @Override
    public List<TrainingType> findAll() {
        return List.copyOf(trainingTypes.values());
    }

    @Override
    public void save(TrainingType type) {
        type.setId(id++);
        trainingTypes.put(type.getId(), type);
    }
}
