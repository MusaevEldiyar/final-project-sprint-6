import controllers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import util.Managers;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Получаем реализацию TaskManager через Managers
        TaskManager taskManager = Managers.getDefault();

        while (true) {
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            switch (command) {
                case 1 -> createTask(taskManager);
                case 2 -> createEpic(taskManager);
                case 3 -> createSubtask(taskManager);
                case 4 -> showAllTasks(taskManager);
                case 5 -> showAllEpics(taskManager);
                case 6 -> showAllSubtasks(taskManager);
                case 7 -> updateTask(taskManager);
                case 8 -> deleteTask(taskManager);
                case 9 -> deleteAllTasks(taskManager);
                case 10 -> showHistory(taskManager);
                case 11 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Меню трекера задач ===");
        System.out.println("1. Создать задачу");
        System.out.println("2. Создать эпик");
        System.out.println("3. Создать подзадачу");
        System.out.println("4. Показать все задачи");
        System.out.println("5. Показать все эпики");
        System.out.println("6. Показать все подзадачи");
        System.out.println("7. Обновить задачу");
        System.out.println("8. Удалить задачу");
        System.out.println("9. Удалить все задачи");
        System.out.println("10. Показать историю просмотров");
        System.out.println("11. Выйти");
        System.out.print("Выберите действие: ");
    }

    private static void createTask(TaskManager taskManager) {
        System.out.print("Введите название задачи: ");
        String name = scanner.nextLine();

        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();

        Task task = new Task(0, name, description, TaskStatus.NEW);
        int taskId = taskManager.addTask(task);

        System.out.println("Задача создана с ID: " + taskId);
    }

    private static void createEpic(TaskManager taskManager) {
        System.out.print("Введите название эпика: ");
        String name = scanner.nextLine();

        System.out.print("Введите описание эпика: ");
        String description = scanner.nextLine();

        Epic epic = new Epic(0, name, description);
        int epicId = taskManager.addTask(epic);  // Добавляем как задачу (Epic — это подтип Task)

        System.out.println("Эпик создан с ID: " + epicId);
    }

    private static void createSubtask(TaskManager taskManager) {
        System.out.print("Введите ID эпика, к которому относится подзадача: ");
        int epicId = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        if (taskManager.getTask(epicId) == null || !(taskManager.getTask(epicId) instanceof Epic)) {
            System.out.println("Эпик с таким ID не найден.");
            return;
        }

        System.out.print("Введите название подзадачи: ");
        String name = scanner.nextLine();

        System.out.print("Введите описание подзадачи: ");
        String description = scanner.nextLine();

        Subtask subtask = new Subtask(0, name, description, TaskStatus.NEW, epicId);
        int subtaskId = taskManager.addTask(subtask);  // Добавляем как задачу (Subtask — это подтип Task)

        System.out.println("Подзадача создана с ID: " + subtaskId);
    }

    private static void showAllTasks(TaskManager taskManager) {
        System.out.println("Все задачи:");
        taskManager.getTasks().forEach(System.out::println);
    }

    private static void showAllEpics(TaskManager taskManager) {
        System.out.println("Все эпики:");
        taskManager.getEpics().forEach(System.out::println);
    }

    private static void showAllSubtasks(TaskManager taskManager) {
        System.out.println("Все подзадачи:");
        taskManager.getSubtasks().forEach(System.out::println);
    }

    private static void deleteAllTasks(TaskManager taskManager) {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
    }

    private static void updateTask(TaskManager taskManager) {
        System.out.print("Введите ID задачи для обновления: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        Task task = taskManager.getTask(id);

        // Если задача не найдена, пробуем искать в других коллекциях (Epic, Subtask)
        if (task == null) {
            task = taskManager.getTask(id);  // Попробуем найти эпик
            if (task == null) {
                task = taskManager.getTask(id);  // Попробуем найти подзадачу
            }
        }

        // Если задача не найдена в целом
        if (task == null) {
            System.out.println("Задача с таким ID не найдена.");
            return;
        }

        TaskStatus status = null;
        while (status == null) {
            System.out.print("Введите новый статус (NEW, IN_PROGRESS, DONE): ");
            String statusInput = scanner.nextLine().trim().toUpperCase();

            switch (statusInput) {
                case "NEW" -> status = TaskStatus.NEW;
                case "IN_PROGRESS" -> status = TaskStatus.IN_PROGRESS;
                case "DONE" -> status = TaskStatus.DONE;
                default -> System.out.println("Некорректный статус. Попробуйте снова.");
            }
        }

        task.setStatus(status);
        taskManager.updateTask(task);
        System.out.println("Задача обновлена.");
    }

    private static void deleteTask(TaskManager taskManager) {
        System.out.print("Введите ID задачи для удаления: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        taskManager.deleteTask(id);
        System.out.println("Задача удалена.");
    }

    private static void showHistory(TaskManager taskManager) {
        System.out.println("История просмотров:");
        taskManager.getHistory().forEach(System.out::println);
    }

}
