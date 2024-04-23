package com.nadya159.service.impl;

import com.nadya159.dao.AuditDao;
import com.nadya159.entity.Audit;
import com.nadya159.entity.types.ActionResult;
import com.nadya159.entity.types.ActionType;
import com.nadya159.service.AuditService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для работы с логированием действий пользователей
 */
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDao auditDao;

    /**
     * Метод добавления лога действий пользователя
     */
    @Override
    public void save(String login, ActionType action, ActionResult result) {
        Audit audit = Audit.builder()
                .login(login)
                .action(action)
                .dateTime(LocalDateTime.now())
                .actionResult(result)
                .build();
        auditDao.save(audit);
    }

    /**
     * Метод получения логов действий пользователя
     *
     * @return logs - весь список логов, доступно только для роли ADMIN
     */
    @Override
    public List<Audit> findAll() {
        List<Audit> logs = auditDao.findAll();
        return logs;
    }
}
