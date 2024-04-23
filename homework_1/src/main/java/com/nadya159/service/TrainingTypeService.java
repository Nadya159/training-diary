package com.nadya159.service;

import com.nadya159.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    List<TrainingType> findAll();

    void save(TrainingType type);
}
