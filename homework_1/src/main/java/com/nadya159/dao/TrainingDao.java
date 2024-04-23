package com.nadya159.dao;

import com.nadya159.entity.Training;

public interface TrainingDao extends BaseDao<Long, Training> {

    void delete(Long id);
}
