package com.nadya159.service.impl;

import com.nadya159.dao.TrainingTypeDao;
import com.nadya159.entity.TrainingType;
import com.nadya159.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeDao.findAll();
    }

    @Override
    public void save(TrainingType type) {
        trainingTypeDao.save(type);
    }
}
