package com.nadya159;

import com.nadya159.controller.MainController;
import com.nadya159.exception.NotValidArgumentException;
import com.nadya159.exception.SecurityException;
import com.nadya159.in.InputData;
import com.nadya159.out.OutputData;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeParseException;

import static com.nadya159.handler.SecurityHandler.handleAuthorization;
import static com.nadya159.handler.SecurityHandler.handleRegistration;
import static com.nadya159.handler.MainHandler.*;

@Slf4j
public class ApplicationRunner {

    private static MainController controller;
    private static UserStage userStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        userStage = UserStage.SECURITY;

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (userStage) {
                    case SECURITY -> menuSecurity(inputData, outputData);
                    case MAIN_MENU -> menuUser(inputData, outputData);
                    case EXIT -> {
                        outputData.output("До новых встреч!");
                        processIsRun = false;
                    }
                }
            } catch (SecurityException | NotValidArgumentException | NumberFormatException |
                     DateTimeParseException e) {         //todo добавить эксепшены
                outputData.errorOutput(e.getMessage());
            } catch (RuntimeException e) {
                outputData.errorOutput("Неизвестная ошибка: " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    /**
     * Основное меню приложения
     * 1 - Вход
     * 2 - Регистрация
     * 3 - Выход из приложения
     */
    public static void menuSecurity(InputData inputData, OutputData outputData) {
        final String mainMenu = "\nОсновное меню (выберите операцию):\n 1 - Вход (авторизация)" +
                "\n 2 - Регистрация\n 3 - Выход из приложения";
        int choice = 3;
        do {
            outputData.output(mainMenu);
            choice = Integer.valueOf((String) inputData.input());
            switch (choice) {
                case 1 -> {
                    userStage = handleAuthorization(inputData, outputData, controller);
                    outputData.output("Вы успешно авторизовались!");
                    return;
                }
                case 2 -> {
                    userStage = handleRegistration(inputData, outputData, controller);
                    outputData.output("Вы успешно зарегистрировались!");
                    return;
                }
                case 3 -> {
                    userStage = UserStage.EXIT;
                    outputData.output("До новых встреч!");
                    return;
                }
                default -> outputData.output("Вы ввели неверное значение, введите число от 1 до 3: " + choice);
            }
        } while (choice != 3);
    }

    /**
     * Меню пользователя в авторизованной зоне
     *
     * @param - текущего пользователя
     */
    public static void menuUser(InputData inputData, OutputData outputData) {
        final String menuUser = "\nМеню пользователя (выберите операцию):" +
                "\n 1 - Добавить тренировку\n 2 - Редактировать тренировку" +
                "\n 3 - Удалить тренировку\n 4 - Просмотр тренировок текущего пользователя" +
                "\n 5 - Добавить новый тип тренировки\n 6 - Статистика по тренировкам" +
                "\n 7 - Просмотр тренировок всех пользователей\n 8 - Аудит действий\n 9 - Возврат в основное меню";

        int choice = 9;
        do {
            outputData.output(menuUser);
            choice = Integer.valueOf((String) inputData.input());
            switch (choice) {
                case 1 -> {
                    addTraining(inputData, outputData, controller);
                    outputData.output("Ваша тренировка успешно добавлена!");
                }
                case 2 -> {
                    updateTraining(inputData, outputData, controller);
                    outputData.output("Ваша тренировка успешно отредактирована!");
                }
                case 3 -> {
                    deleteTraining(inputData, outputData, controller);
                    outputData.output("Ваша тренировка успешно удалена!");
                }
                case 4 -> showTrainingsByUser(outputData, controller);
                case 5 -> {
                    addTypeTraining(inputData, outputData, controller);
                    outputData.output("Новый тип тренировки успешно добавлен!");
                }
                //case 6 -> trainingService.statistic();
                case 7 -> showAllTraining(outputData, controller);
                case 8 -> showLogs(outputData, controller);
                case 9 -> {
                    userStage = UserStage.SECURITY;
                    return;
                }
                default -> outputData.output("Вы ввели неверное значение, введите число от 1 до 9: " + choice);
            }
        } while (choice != 9);
    }
}
