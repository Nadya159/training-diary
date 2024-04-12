package com.nadya159.in.Service;

import com.nadya159.entity.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с логированием действий пользователей
 */
public class AuditService {

    private static final AuditService INSTANCE = new AuditService();
    private static final List<Log> logs = new ArrayList<>();


    /**
     * Метод добавления лога действий пользователя
     */
    public void add(String action, String email, String result) {
        Log log = Log.builder()
                .action(action)
                .dateTime(LocalDateTime.now())
                .email(email)
                .actionResult(result)
                .build();
        logs.add(log);
    }

    /**
     * Метод получения логов действий пользователя
     */
    public void printAll() {
        System.out.println("Лог действий пользователей:");
        for (Log log : logs) {
            System.out.println("Действие: " + log.getAction() + " Дата: " + log.getDateTime() +
                    " Пользователь: " + log.getEmail() + " Результат: " + log.getActionResult());
        }
    }

    /**
     * Метод получения Instance текущего объекта
     *
     * @return INSTANCE текущего объекта
     */
    public static AuditService getInstance() {
        return INSTANCE;
    }
}
