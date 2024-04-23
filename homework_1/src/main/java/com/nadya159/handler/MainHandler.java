package com.nadya159.handler;

import com.nadya159.ApplicationContext;
import com.nadya159.controller.MainController;
import com.nadya159.entity.Audit;
import com.nadya159.entity.Training;
import com.nadya159.entity.TrainingType;
import com.nadya159.entity.types.Role;
import com.nadya159.exception.NotValidArgumentException;
import com.nadya159.exception.SecurityException;
import com.nadya159.in.InputData;
import com.nadya159.out.OutputData;

import java.time.LocalDate;
import java.util.List;

public class MainHandler {

    //Добавление тренировки
    public static void addTraining(InputData inputData, OutputData outputData, MainController controller) {
        outputData.output("Выберите тип тренировки:");
        showTrainingTypes(outputData, controller);
        int inputTypeId = Integer.valueOf((String) inputData.input());
        outputData.output("Введите дату тренировки (формат ГГГГ-ММ-ДД):");
        LocalDate date = LocalDate.parse(inputData.input().toString());
        outputData.output("Введите длительность тренировки в минутах:");
        int period = Integer.valueOf((String) inputData.input());
        outputData.output("Введите количество потраченных калорий:");
        int calories = Integer.valueOf((String) inputData.input());
        outputData.output("Введите дополнительную информацию:");
        String description = inputData.input().toString();
        controller.createTraining(ApplicationContext.getAuthorizeUser().getId(), date, inputTypeId, period, calories, description);
    }

    //Создание нового типа тренировки
    public static void addTypeTraining(InputData inputData, OutputData outputData, MainController controller) {
        outputData.output("Введите название нового типа:");
        String nameNewType = (String) inputData.input();
        if (!controller.findAllTrainingTypes().contains(nameNewType) && !nameNewType.isBlank()) {
            controller.createTrainingType(nameNewType);
        }
    }

    /**
     * Вывод всех типов тренировок
     *
     * @param outputData
     * @param controller
     */
    public static void showTrainingTypes(OutputData outputData, MainController controller) {
        List<TrainingType> types = controller.findAllTrainingTypes();
        for (TrainingType type : types) {
            outputData.output(formatTrainingType(type));
        }
    }

    /**
     * Редактирование тренировки
     */
    public static void updateTraining(InputData inputData, OutputData outputData, MainController controller) {
        if (isTrainingExist(controller)) {
            showTrainingsByUser(outputData, controller);
            outputData.output("Введите ID (номер) тренировки для редактирования:");
            Long trainingID = Long.valueOf((String) inputData.input());
            if (controller.findTrainingById(trainingID).isPresent()) {
                outputData.output("Введите новое значение (длительность тренировки в минутах):");
                int period = Integer.valueOf((String) inputData.input());
                outputData.output("Введите новое значение (количество потраченных калорий):");
                int calories = Integer.valueOf((String) inputData.input());
                outputData.output("Введите новое значение (дополнительная информация):");
                String description = inputData.input().toString();
                controller.updateTraining(ApplicationContext.getAuthorizeUser().getId(), trainingID, period, calories, description);
            } else outputData.errorOutput("Некорректный ID (номер) тренировки!");
        }
    }

    /**
     * Удаление тренировки
     *
     * @param outputData
     * @param controller
     */
    public static void deleteTraining(InputData inputData, OutputData outputData, MainController controller) {
        if (isTrainingExist(controller)) {
            showTrainingsByUser(outputData, controller);
            outputData.output("Введите ID (номер) тренировки для удаления:");
            Long trainingID = Long.valueOf((String) inputData.input());
            if (controller.findTrainingById(trainingID).isPresent()) {
                controller.deleteTraining(trainingID);
            } else outputData.errorOutput("Некорректный ID (номер) тренировки!");
        }
    }

    //история тренировок
    public static void showTrainingsByUser(OutputData outputData, MainController controller) {
        List<Training> trainings = controller.findTrainingsByUserId(ApplicationContext.getAuthorizeUser().getId());
        List<TrainingType> trainingTypes = controller.findAllTrainingTypes();
        if (trainings.isEmpty()) outputData.output("Данные по тренировкам отсутствуют!");
        else {
            outputData.output("Список тренировок:");
            for (Training training : trainings)
                outputData.output(formatTraining(training, trainingTypes));
        }
    }

    /**
     * Вывод статистики тренировок пользователя
     * - статистика потраченных калорий за выбранный месяц
     */
    public static void showStatistics(InputData inputData, OutputData outputData, MainController controller) {
        List<Training> trainings = controller.findTrainingsByUserId(ApplicationContext.getAuthorizeUser().getId());
        if (!trainings.isEmpty()) {
            outputData.output("Введите 'Дата с' (формат ГГГГ-ММ-ДД):");
            LocalDate dateFrom = LocalDate.parse(inputData.input().toString());
            outputData.output("Введите 'Дата до' (формат ГГГГ-ММ-ДД):");
            LocalDate dateTo = LocalDate.parse(inputData.input().toString());
            outputData.output("Статистика тренировок:");
            //trainings.stream().filter(training -> training.getDate() > dateFrom &&  )

        } else throw new NotValidArgumentException("Данные по тренировкам отсутствуют!");
    }

    private static boolean isTrainingExist(MainController controller) {
        List<Training> trainings = controller.findTrainingsByUserId(ApplicationContext.getAuthorizeUser().getId());
        if (trainings.isEmpty()) {
            throw new NotValidArgumentException("Данные по тренировкам отсутствуют!");
        } else return true;
    }

    public static void showAllTraining(OutputData outputData, MainController controller) {
        if (isUserAdmin(controller)) {
            List<Training> trainings = controller.findAllTrainings();
            if (trainings.isEmpty()) outputData.output("Данные по тренировкам отсутствуют!");
            else {
                List<TrainingType> trainingTypes = controller.findAllTrainingTypes();
                outputData.output("Список тренировок:");
                for (Training training : trainings)
                    outputData.output(formatTraining(training, trainingTypes));
            }
        } else throw new SecurityException("Просмотр тренировок всех пользователей доступен роли 'Администратор'");
    }

    /**
     * Аудит действий пользователей
     *
     * @param outputData
     * @param controller
     */
    public static void showLogs(OutputData outputData, MainController controller) {
        if (isUserAdmin(controller)) {
            List<Audit> allAudits = controller.getLogs();
            outputData.output("Лог действий пользователей:");
            for (Audit audit : allAudits) {
                outputData.output(formatAudit(audit));
            }
        } else throw new SecurityException("Просмотр аудита доступен роли 'Администратор'");
    }

    private static boolean isUserAdmin(MainController controller) {
        if (ApplicationContext.getAuthorizeUser().getRole().equals(Role.ADMIN))
            return true;
        else return false;
    }

    private static String formatTraining(Training training, List<TrainingType> types) {
        TrainingType type = types.get(training.getTypeId() - 1);
        return String.format("%s | %s | %s | %s | %s | %s",
                training.getId(), training.getDate(), type.getTypeName(), training.getCalories(),
                training.getPeriod(), training.getDescription());
    }

    public static String formatTrainingType(TrainingType type) {
        return String.format("%s. %s", type.getId(), type.getTypeName());
    }

    private static String formatAudit(Audit audit) {
        return String.format("%s | %s | %s",                        //todo почему дата string
                audit.getDateTime(), audit.getAction(), audit.getActionResult(), audit.getLogin());
    }
}
