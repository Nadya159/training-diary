package com.nadya159.in.Service;

import com.nadya159.entity.Role;
import com.nadya159.entity.User;
import com.nadya159.in.Menu.ResultInput;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Класс, реализующий бизнес-логику приложения по работе с пользователями
 */
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private static final HashMap<String, User> users = new HashMap<>();
    private final AuditService audit = AuditService.getInstance();
    private static String userEmail = "";

    /**
     * Создание первоначального списка пользователей
     */
    public void init() {
        users.put("admin@gmail.com", new User("admin", "admin@gmail.com", "admin", Role.ADMIN));
        users.put("user1@gmail.com", new User("user1", "user1@gmail.com", "123", Role.USER));
        users.put("user2@gmail.com", new User("user2", "user2@gmail.com", "123", Role.USER));
    }

    /**
     * Вход (авторизация) пользователя
     */
    public ResultInput input() {
        ResultInput resultInput = ResultInput.BREAKING;
        int attempt = 1;
        Scanner console = new Scanner(System.in);
        System.out.println("Введите ваш email");
        String email = console.next();

        if (users.containsKey(email)) {
            userEmail = email;
            System.out.println("Введите ваш пароль");
            String pswd = console.next();

            while (attempt <= 3) {
                if (!users.get(email).getPassword().equals(pswd)) {
                    attempt++;
                    System.out.printf("Вы ввели неверный пароль! Введите еще раз. Попытка %d из 3 \n", attempt);
                    pswd = console.next();
                    if (attempt == 3) {
                        System.out.println("Вы ввели неверный пароль 3 раза! Выход из приложения\n");
                        resultInput = ResultInput.BREAKING;
                        audit.add("Wrong password input 3 times", email, "FAIL");
                        break;
                    }
                } else {
                    break;
                }
            }
            if (users.get(email).getPassword().equals(pswd) && users.get(email).getRole().equals(Role.ADMIN)) {
                System.out.println("Вы вошли как администратор");
                audit.add("Input in app as ADMIN", email, "SUCCESS");
                return resultInput = ResultInput.ADMIN;
            }
            if (users.get(email).getPassword().equals(pswd) && users.get(email).getRole().equals(Role.USER)) {
                System.out.println("Вы вошли как пользователь");
                audit.add("Input in app as USER", email, "SUCCESS");
                return resultInput = ResultInput.USER;
            }
        } else {
            System.out.println("Пользователя с таким email не существует, введите данные для создания учетной записи");
            registration(email);
            resultInput = ResultInput.USER;
        }
        return resultInput;
    }

    /**
     * Регистрация нового пользователя
     *
     * @param email введенный пользователем email
     */
    public void registration(String email) {
        Scanner console = new Scanner(System.in);
        if (email.isBlank()) {
            System.out.println("Введите email");
            email = console.next();     //todo добавить валидацию email регуляркой
        }
        System.out.println("Введите пароль");
        String password = console.next();
        System.out.println("Введите имя");
        String name = console.next();
        users.put(email, new User(email, name, password, Role.USER));
        userEmail = email;
        audit.add("Registration in app as USER", email, "SUCCESS");
        System.out.println("Спасибо за вашу регистрацию, " + name + " !");
    }

    /**
     * Метод получения email текущего пользователя
     *
     * @return userEmail - email текущего пользователя
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Метод получения роли текущего пользователя
     *
     * @return Role - роль текущего пользователя
     */
    public Role getUserRole() {
        return users.get(userEmail).getRole();
    }

    /**
     * Метод получения Instance текущего объекта
     *
     * @return INSTANCE текущего объекта
     */
    public static UserService getInstance() {
        return INSTANCE;
    }
}
