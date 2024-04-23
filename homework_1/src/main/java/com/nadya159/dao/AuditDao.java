package com.nadya159.dao;

import com.nadya159.entity.Audit;

import java.util.List;
import java.util.Optional;

public interface AuditDao extends BaseDao<Long, Audit> {

    Optional<Audit> findById(Long id);

    List<Audit> findAll();

    void save(Audit e);
}
