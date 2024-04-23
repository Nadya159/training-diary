package com.nadya159.service.impl;

import com.nadya159.dao.TrainingDao;
import com.nadya159.entity.Training;
import com.nadya159.entity.TrainingType;
import com.nadya159.entity.User;
import com.nadya159.entity.types.ActionResult;
import com.nadya159.entity.types.ActionType;
import com.nadya159.exception.NotValidArgumentException;
import com.nadya159.exception.SecurityException;
import com.nadya159.service.AuditService;
import com.nadya159.service.TrainingService;
import com.nadya159.service.TrainingTypeService;
import com.nadya159.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, реализующий бизнес-логику приложения по работе с тренировками
 */
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDao;
    private final UserService userService;
    private final AuditService auditService;
    private final TrainingTypeService trainingTypeService;

    /**
     * Поиск тренировок пользователя
     */
    @Override
    public List<Training> findByUserId(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new SecurityException("Пользователь с ID: " + userId + " не найден"));
        List<Training> allTrainings = trainingDao.findAll();
        List<Training> userTrainings = allTrainings.stream()
                .filter(training -> training.getUserId().equals(userId)).toList();  //todo тренировки отсутствуют
        auditService.save(user.getLogin(), ActionType.GET_HISTORY,
                (userTrainings == null) ? ActionResult.FAIL : ActionResult.SUCCESS);
        return userTrainings;
    }

    /**
     * Поиск тренировки по ID
     */
    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(trainingDao.findById(id)
                .orElseThrow(() -> new NotValidArgumentException("Тренировка с ID: " + id + " не найдена")));
    }

    /**
     * Поиск тренировок всех пользователей
     */
    @Override
    public List<Training> findAll() {
        List<Training> allTrainings = trainingDao.findAll();
        /*auditService.save(user.getLogin(), ActionType.GET_HISTORY,
                (userTrainings == null) ? ActionResult.FAIL : ActionResult.SUCCESS);*/
        return allTrainings;
    }

    /**
     * Обновление тренировки
     */
    @Override
    public void update(Long userId, Long id, Integer period, Integer calories, String description) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new SecurityException("Пользователь с ID: " + userId + " не найден"));

        Optional<Training> training = trainingDao.findById(id);
        if (training.isPresent()) {
            if (period > 0) training.get().setPeriod(LocalTime.ofSecondOfDay(period));
            if (calories > 0) training.get().setCalories(calories);
            if (!description.isBlank()) training.get().setDescription(description);
        }
        trainingDao.save(training.get());
        auditService.save(user.getLogin(), ActionType.SUBMIT_TRAINING, ActionResult.SUCCESS);

    }

    /**
     * Создание новой тренировки
     */
    @Override
    public void create(Long userId, LocalDate date, Integer typeId, Integer period, Integer calories, String description) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new SecurityException("Пользователь с ID: " + userId + " не найден"));

        if (date == null || period == null || calories == null) {       //todo запись в лог?
            throw new NotValidArgumentException("Пожалуйста, заполните все пустые поля");
        }

        //проверка на тип тренировки
        List<TrainingType> allTypes = trainingTypeService.findAll();
        if (typeId <= 0 || typeId > allTypes.size()) {      //todo запись в лог?
            throw new NotValidArgumentException("Неверный тип тренировки");
        }

        //проверка указанного типа на наличие за этот день
        List<Training> userTrainings = findByUserId(userId);
        Set<Integer> existingTypesByDate = userTrainings.stream()
                .filter(training -> training.getDate().equals(date))
                .map(Training::getTypeId).collect(Collectors.toSet());
        if (existingTypesByDate.contains(typeId)) {
            throw new NotValidArgumentException("За выбранный день указанный тип тренировки уже есть");
        }

        Training training = Training.builder()
                .userId(userId)
                .date(date)
                .typeId(typeId)
                .period(LocalTime.of(period / 60, period % 60))
                .calories(calories)
                .description(description)
                .build();
        trainingDao.save(training);
        auditService.save(user.getLogin(), ActionType.SUBMIT_TRAINING, ActionResult.SUCCESS);
    }

    @Override
    public void delete(Long id) {
        trainingDao.delete(id);
    }

    @Override
    public List<Training> getTrainingsByMonthAndYear(Integer year, Integer month, Long userId) {
        return List.of();
    }

    @Override
    public List<Training> getTrainingHistory(Long userId) {
        return List.of();
    }
}
