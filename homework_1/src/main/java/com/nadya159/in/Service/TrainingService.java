package com.nadya159.in.Service;

import com.nadya159.entity.Role;
import com.nadya159.entity.Training;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, реализующий бизнес-логику приложения по работе с тренировками
 */
public class TrainingService {
    private static final TrainingService INSTANCE = new TrainingService();
    private final UserService userService = UserService.getInstance();
    private static final HashMap<Integer, String> types = new HashMap<>();
    private static final List<Training> trainings = new ArrayList<>();
    private final AuditService audit = AuditService.getInstance();

    /**
     * Добавление тренировки пользователя
     */
    public void create() {
        Scanner console = new Scanner(System.in);
        System.out.println("Введите дату тренировки (формат ГГГГ-ММ-ДД):");
        LocalDate date = LocalDate.parse(console.next());

        System.out.println("Выберите тип тренировки:");
        for (int i = 0; i < types.size(); i++) {
            System.out.println(i + " - " + types.get(i));
        }
        System.out.println(types.size() + " - Создать новую");
        Integer inputTypeId = console.nextInt();

        if (inputTypeId == types.size()) {
            System.out.println("Введите наименование нового типа тренировки:");
            String newNameType = console.next();
            types.put(types.size(), newNameType);

        } else if (inputTypeId < types.size()) {
            Set<Integer> existingTypesByDate = trainings.stream()   //список уже существующих типов за выбранный день
                    .filter(training -> training.getDate().equals(date))
                    .map(Training::getTypeId).collect(Collectors.toSet());
            if (!existingTypesByDate.contains(inputTypeId)) {       //проверка указанного типа на наличие за этот день
                System.out.println("Введите длительность тренировки в минутах:");
                int period = console.nextInt();
                System.out.println("Введите количество потраченных калорий:");
                int calories = console.nextInt();
                System.out.println("Введите дополнительную информацию:");
                String description = console.next();

                Training userTraining = Training.builder()
                        .email(userService.getUserEmail())
                        .date(date)
                        .typeId(inputTypeId)
                        .period(LocalTime.of(period / 60, period % 60))
                        .calories(calories)
                        .description(description)
                        .build();
                trainings.add(userTraining);
                audit.add("Add new training", userService.getUserEmail(), "SUCCESS");
                System.out.println("Ваша тренировка успешно добавлена!");
            } else {
                audit.add("Attempt add new training", userService.getUserEmail(), "FAIL");
                System.out.println("За выбранный день указанный тип тренировки уже есть! Возврат в меню");
            }
        } else {
            audit.add("Attempt add new training", userService.getUserEmail(), "FAIL");
            System.out.println("Введен некорректный тип тренировки! Возврат в меню");
        }
    }

    /**
     * Получение тренировок пользователя с сортировкой по дате
     *
     * @return List<Training> список тренировок текущего пользователя (сортировка по дате)
     */
    public List<Training> findAllByEmail() {
        //фильтр всех тренировок по email пользователя и сортировка по дате
        List<Training> userTrainings = trainings.stream()
                .filter(training -> training.getEmail().equals(userService.getUserEmail()))
                .sorted(Comparator.comparing(Training::getDate)).toList();

        System.out.println(userService.getUserEmail() + ", дневник ваших тренировок:");
        for (Training item : userTrainings) {
            if (item.getEmail().equals(userService.getUserEmail()))
                System.out.println("Дата: " + item.getDate() + " Тип тренировки: " + item.getTypeId() + //todo вывод имя
                        " Длительность (ЧЧ:ММ): " + item.getPeriod() + " Количество калорий: " + item.getCalories() +
                        " Доп. описание: " + item.getDescription());
        }
        return userTrainings;
    }

    /**
     * Получение всех тренировок всех пользователей
     * условие: наличие у пользователя роли ADMIN
     */
    public void findAll() {
        if (userService.getUserRole() == Role.ADMIN) {
            //сортировка всех тренировок сначала по пользователю, затем по дате
            List<Training> sortedTrainings = trainings.stream().sorted(Comparator.comparing(Training::getDate))
                    .sorted(Comparator.comparing(Training::getEmail)).toList();
            for (Training item : sortedTrainings) {
                System.out.println("Пользователь: " + item.getEmail() + "Дата: " + item.getDate() +
                        " Тип тренировки: " + item.getTypeId() + " Длительность (ЧЧ:ММ): " + item.getPeriod() +
                        " Количество калорий: " + item.getCalories() + " Доп. описание: " + item.getDescription());
            }
        } else {
            System.out.println("У вас отсутствует доступ на просмотр!");

        }
    }

    /**
     * Вывод статистики тренировок пользователя
     * - статистика потраченных калорий за текущий месяц
     */
    public void statistic() {
        List<Training> userTrainings = findAllByEmail();
        if (!userTrainings.isEmpty()) {             //есть ли у пользователя тренировки
            var summa = userTrainings.stream()
                    .filter(training -> training.getDate().getMonth().equals(LocalDate.now().getMonth()))
                    .collect(Collectors.summarizingInt(Training::getCalories));
            System.out.println("Статистика потраченных калорий в текущем месяце = " + summa);
        } else
            System.out.println("У вас отсутствуют тренировки! Возврат в меню");
        audit.add("Get statistic trainings", userService.getUserEmail(), "SUCCESS");
    }

    /**
     * Удаление тренировки пользователя
     */
    public void delete() {
        List<Training> userTrainings = findAllByEmail();
        if (!userTrainings.isEmpty()) {             //есть ли у пользователя тренировки
            Scanner console = new Scanner(System.in);
            System.out.println("Введите порядковый номер вашей тренировки для удаления");
            int number = console.nextInt();
            if (number > 0 && number <= userTrainings.size()) {
                var training = userTrainings.remove(number);
                trainings.remove(training);
                audit.add("Delete training", userService.getUserEmail(), "SUCCESS");
            } else {
                audit.add("Attempt delete training", userService.getUserEmail(), "FAIL");
                System.out.println("Введен неверный порядковый номер вашей тренировки! Возврат в меню");
            }
        } else
            System.out.println("У вас отсутствуют тренировки! Возврат в меню");
    }

    /**
     * Редактирование тренировки пользователя
     */
    public void update() {
        List<Training> userTrainings = findAllByEmail();
        if (!userTrainings.isEmpty()) {             //есть ли у пользователя тренировки
            Scanner console = new Scanner(System.in);
            System.out.println("Введите порядковый номер вашей тренировки для редактирования");
            int number = console.nextInt();             //todo добавить логику
            create();
        } else
            System.out.println("У вас отсутствуют тренировки! Возврат в меню");
    }

    /**
     * Создание первоначальных данных о тренировках:
     * - справочник видов (типов) тренировок
     * - история 3-х тренировок пользователя user1, user2
     */
    public void init() {
        // Создание первоначального справочника вида (типов) тренировок
        types.put(0, "Yoga");
        types.put(1, "Cardio");
        types.put(2, "Power");

        //Создание первоначальной истории тренировок пользователя user1
        Training training1 = Training.builder()
                .email("user1@gmail.com")
                .date(LocalDate.of(2024, 4, 10))
                .typeId(0)
                .period(LocalTime.of(1, 15))
                .calories(112)
                .description("Растяжка на всех группы мышц")
                .build();

        Training training2 = Training.builder()
                .email("user1@gmail.com")
                .date(LocalDate.of(2024, 4, 5))
                .typeId(1)
                .period(LocalTime.of(0, 50))
                .calories(207)
                .description("Бег, отжимания, прыжки")
                .build();

        //Создание первоначальной истории тренировок пользователя user2
        Training training3 = Training.builder()
                .email("user2@gmail.com")
                .date(LocalDate.of(2024, 4, 7))
                .typeId(2)
                .period(LocalTime.of(1, 0))
                .calories(232)
                .description("Штанга, гири")
                .build();

        trainings.addAll(List.of(training1, training2, training3));
    }

    /**
     * Метод получения instance текущего объекта
     *
     * @return INSTANCE текущего объекта
     */
    public static TrainingService getInstance() {
        return INSTANCE;
    }
}
