package com.nadya159.service;

import com.nadya159.entity.Audit;
import com.nadya159.entity.types.ActionResult;
import com.nadya159.entity.types.ActionType;

import java.util.List;

/**
 * интерфейс сервиса для логирования действий пользователя
 */
public interface AuditService {

    void save(String login, ActionType action, ActionResult result);

    List<Audit> findAll();
}
