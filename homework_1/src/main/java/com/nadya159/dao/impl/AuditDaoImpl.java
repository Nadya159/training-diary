package com.nadya159.dao.impl;

import com.nadya159.dao.AuditDao;
import com.nadya159.entity.Audit;

import java.util.*;

public class AuditDaoImpl implements AuditDao {

    private final Map<Long, Audit> logs = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<Audit> findById(Long id) {
        Audit audit = logs.get(id);
        return Optional.ofNullable(audit);
    }

    @Override
    public List<Audit> findAll() {
        return List.copyOf(logs.values());
    }

    @Override
    public void save(Audit audit) {
        audit.setId(id++);
        logs.put(audit.getId(), audit);
    }
}
