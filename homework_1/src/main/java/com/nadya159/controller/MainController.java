package com.nadya159.controller;

import com.nadya159.entity.Audit;
import com.nadya159.entity.Training;
import com.nadya159.entity.TrainingType;
import com.nadya159.entity.User;
import com.nadya159.service.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MainController {
    private final SecurityService securityService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;
    private final AuditService auditService;

    public User register(String login, String password) {
        return securityService.register(login, password);
    }

    public Optional<User> authorize(String login, String password) {
        return securityService.authorize(login, password);
    }

    public List<Training> findTrainingsByUserId(Long userId) {
        return trainingService.findByUserId(userId);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingService.findById(id);
    }

    public void createTraining(Long userId, LocalDate date, Integer typeId, Integer period,
                               Integer calories, String description) {
        trainingService.create(userId, date, typeId, period, calories, description);
    }

    public void updateTraining(Long userId, Long id, Integer period, Integer calories, String description) {
        trainingService.update(userId, id, period, calories, description);
    }

    public List<TrainingType> findAllTrainingTypes() {
        return trainingTypeService.findAll();
    }

    public void createTrainingType(String newType) {
        trainingTypeService.save(TrainingType.builder().typeName(newType).build());
    }

    public void deleteTraining(Long trainingID) {
        trainingService.delete(trainingID);
    }

    public List<Audit> getLogs() {
        return auditService.findAll();
    }
}
