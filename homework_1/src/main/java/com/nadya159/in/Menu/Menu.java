package com.nadya159.in.Menu;

import com.nadya159.in.Service.AuditService;
import com.nadya159.in.Service.TrainingService;
import com.nadya159.in.Service.UserService;
import lombok.NoArgsConstructor;

import java.util.Scanner;

/**
 * Класс основного меню приложения и меню пользователя в авторизованной зоне
 */
@NoArgsConstructor
public class Menu {
    private final UserService userService = UserService.getInstance();
    private final TrainingService trainingService = TrainingService.getInstance();
    private final AuditService auditService = AuditService.getInstance();

    /**
     * Основное меню приложения
     * 1 - Вход / регистрация
     * 2 - Выход из приложения
     */
    public void menuMain() {
        userService.init();
        String email = "";
        int choice = 2;
        do {
            System.out.println("Основное меню:\n Выберите операцию:\n 1 - Вход/регистрация\n 2 - Выход из приложения");
            Scanner console = new Scanner(System.in);
            choice = console.nextInt();
            switch (choice) {
                case 1 -> {
                    switch (userService.input()) {
                        case ADMIN, USER -> menuUser(email);
                        case BREAKING -> {
                            return;
                        }
                    }
                }
                case 2 -> {
                    System.out.println("До новых встреч!");
                    return;
                }
                default -> throw new IllegalStateException(
                        "Вы ввели неверное значение, введите число от 1 до 2: " + choice);
            }
        } while (choice != 2);
    }

    /**
     * Меню пользователя в авторизованной зоне
     *
     * @param email - текущего пользователя
     */
    private void menuUser(String email) {
        trainingService.init();
        int choice = 8;
        do {
            System.out.println("Выберите операцию:\n 1 - Добавить тренировку\n 2 - Редактировать тренировку" +
                    "\n 3 - Удалить тренировку\n 4 - Просмотр тренировок текущего пользователя" +
                    "\n 5 - Просмотр всех тренировок\n 6 - Статистика по тренировкам" +
                    "\n 7 - Аудит действий\n 8 - Возврат в основное меню");
            Scanner console = new Scanner(System.in);
            choice = console.nextInt();
            switch (choice) {
                case 1 -> trainingService.create();
                case 2 -> trainingService.update();
                case 3 -> trainingService.delete();
                case 4 -> trainingService.findAllByEmail();
                case 5 -> trainingService.findAll();
                case 6 -> trainingService.statistic();
                case 7 -> auditService.printAll();
                case 8 -> {
                    return;
                }
                default -> throw new IllegalStateException(
                        "Вы ввели неверное значение, введите число от 1 до 8: " + choice);
            }
        } while (choice != 8);
    }
}
