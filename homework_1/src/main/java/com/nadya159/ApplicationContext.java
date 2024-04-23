package com.nadya159;

import com.nadya159.controller.MainController;
import com.nadya159.dao.AuditDao;
import com.nadya159.dao.TrainingDao;
import com.nadya159.dao.TrainingTypeDao;
import com.nadya159.dao.UserDao;
import com.nadya159.dao.impl.AuditDaoImpl;
import com.nadya159.dao.impl.TrainingDaoImpl;
import com.nadya159.dao.impl.TrainingTypeDaoImpl;
import com.nadya159.dao.impl.UserDaoImpl;
import com.nadya159.entity.Training;
import com.nadya159.entity.TrainingType;
import com.nadya159.entity.User;
import com.nadya159.entity.types.Role;
import com.nadya159.in.ConsoleInputData;
import com.nadya159.out.ConsoleOutputData;
import com.nadya159.service.*;
import com.nadya159.service.impl.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new HashMap<>();
    private static final String AUTHORIZE = "authorize";
    private static final String SECURITY_SERVICE = "securityService";
    private static final String AUDIT_SERVICE= "auditService";
    private static final String TRAINING_SERVICE = "trainingService";
    private static final String TRAINING_TYPE_SERVICE = "trainingTypeService";
    private static final String AUDIT_DAO = "auditDao";
    private static final String USER_SERVICE = "userService";
    private static final String USER_DAO = "userDao";
    private static final String TRAINING_DAO = "trainingDao";
    private static final String TRAINING_TYPE_DAO = "trainingTypeDao";

    /**
     * Loads the application context with DAO, Service, Controller, and Input/Output components.
     */
    public static void loadContext() {
        loadDaoLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    /**
     * Loads an authorized user into the context.
     *
     * @param user The authorized user.
     */
    public static void loadAuthorizeUser(User user) {
        CONTEXT.put(AUTHORIZE, user);
    }

    /**
     * Removes the authorized user from the context.
     */
    public static void cleanAuthorizeUser() {
        CONTEXT.remove(AUTHORIZE);
    }

    /**
     * Retrieves the authorized user from the context.
     *
     * @return The authorized user.
     */
    public static User getAuthorizeUser() {
        return (User) CONTEXT.get(AUTHORIZE);
    }

    /**
     * Retrieves a bean from the context by its name.
     *
     * @param beanName The name of the bean.
     * @return The bean object.
     */
    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadControllers() {
        MainController controller = new MainController(
                (SecurityService) CONTEXT.get(SECURITY_SERVICE),
                (TrainingService) CONTEXT.get(TRAINING_SERVICE),
                (TrainingTypeService) CONTEXT.get(TRAINING_TYPE_SERVICE),
                (UserService) CONTEXT.get(USER_SERVICE),
                (AuditService) CONTEXT.get(AUDIT_SERVICE)
        );
        CONTEXT.put("controller", controller);
    }

    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }

    private static void loadDaoLayer() {
        CONTEXT.put(USER_DAO, new UserDaoImpl());
        CONTEXT.put(TRAINING_DAO, new TrainingDaoImpl());
        CONTEXT.put(TRAINING_TYPE_DAO, new TrainingTypeDaoImpl());
        CONTEXT.put(AUDIT_DAO, new AuditDaoImpl());
    }

    private static void loadServiceLayer() {
        AuditService auditService = new AuditServiceImpl((AuditDao) CONTEXT.get(AUDIT_DAO));
        CONTEXT.put(AUDIT_SERVICE, auditService);

        SecurityService securityService = new SecurityServiceImpl(
                (UserDao) CONTEXT.get(USER_DAO),
                (AuditService) CONTEXT.get(AUDIT_SERVICE)
        );
        CONTEXT.put(SECURITY_SERVICE, securityService);

        UserService userService = new UserServiceImpl((UserDao) CONTEXT.get(USER_DAO));
        CONTEXT.put(USER_SERVICE, userService);

        TrainingTypeService trainingTypeService = new TrainingTypeServiceImpl((TrainingTypeDao) CONTEXT.get(TRAINING_TYPE_DAO));
        CONTEXT.put(TRAINING_TYPE_SERVICE, trainingTypeService);

        TrainingService trainingService = new TrainingServiceImpl(
                (TrainingDao) CONTEXT.get(TRAINING_DAO),
                (UserService) CONTEXT.get(USER_SERVICE),
                (AuditService) CONTEXT.get(AUDIT_SERVICE),
                (TrainingTypeService) CONTEXT.get(TRAINING_TYPE_SERVICE)
        );
        CONTEXT.put(TRAINING_SERVICE, trainingService);
    }
}
