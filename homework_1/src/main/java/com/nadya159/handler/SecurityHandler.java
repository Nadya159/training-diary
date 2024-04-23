package com.nadya159.handler;

import com.nadya159.UserStage;
import com.nadya159.controller.MainController;
import com.nadya159.entity.User;
import com.nadya159.entity.types.Role;
import com.nadya159.in.InputData;
import com.nadya159.out.OutputData;
import com.nadya159.wrapper.SecurityWrapper;
import com.nadya159.ApplicationContext;

public class SecurityHandler {

    public static UserStage handleRegistration(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper wrapper = requestAccount(inputData, outputData);
        User registeredUser = controller.register(wrapper.getLogin(), wrapper.getPassword());
        ApplicationContext.loadAuthorizeUser(registeredUser);
        return UserStage.MAIN_MENU;
    }

    public static UserStage handleAuthorization(InputData inputData, OutputData outputData, MainController controller) {
        SecurityWrapper wrapper = requestAccount(inputData, outputData);
        User authorizedUser = controller.authorize(wrapper.getLogin(), wrapper.getPassword()).orElse(null);

        if (authorizedUser != null) {
            ApplicationContext.loadAuthorizeUser(authorizedUser);
            return isAdmin(authorizedUser) ? UserStage.MAIN_MENU : UserStage.MAIN_MENU; //todo просто сообщение
        }

        return UserStage.MAIN_MENU;
    }

    private static SecurityWrapper requestAccount(InputData inputData, OutputData outputData) {
        String message = "Введите логин (email):";
        outputData.output(message);
        String login = inputData.input().toString();

        message = "Введите пароль:";
        outputData.output(message);
        String password = inputData.input().toString();

        return SecurityWrapper.builder()
                .login(login)
                .password(password)
                .build();
    }

    public static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }
}
