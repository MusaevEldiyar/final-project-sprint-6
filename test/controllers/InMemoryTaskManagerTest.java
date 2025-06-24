package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private final TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        // Создаём задачу
        Task task = new Task(0, "Test addNewTask", "Test addNewTask description", TaskStatus.NEW);

        // Добавляем задачу и получаем её ID
        final int taskId = taskManager.addTask(task);

        // Извлекаем задачу по ID
        final Task savedTask = taskManager.getTask(taskId);

        // Проверяем, что задача найдена
        assertNotNull(savedTask, "Задача не найдена.");

        // Проверяем, что задача совпадает
        assertEquals(task, savedTask, "Задачи не совпадают.");

        // Проверяем, что задачи в списке возвращаются
        final ArrayList<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic(0, "Epic Test", "Epic Test Description");

        // Добавляем эпик как задачу
        int epicId = taskManager.addTask(epic);

        // Извлекаем эпик по ID
        Epic savedEpic = (Epic) taskManager.getTask(epicId);  // Приводим к типу Epic
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic(0, "Epic for Subtask", "Epic for Subtask Description");

        // Добавляем эпик как задачу
        int epicId = taskManager.addTask(epic);

        // Создаём подзадачу
        Subtask subtask = new Subtask(0, "Subtask Test", "Subtask Test Description", TaskStatus.NEW, epicId);

        // Добавляем подзадачу как задачу
        int subtaskId = taskManager.addTask(subtask);

        // Извлекаем подзадачу по ID
        Subtask savedSubtask = (Subtask) taskManager.getTask(subtaskId);  // Приводим к типу Subtask
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }


    @Test
    void deleteTask() {
        // Создаём задачу
        Task task = new Task(0, "Test Task for Deletion", "Test Task for Deletion description", TaskStatus.NEW);
        int taskId = taskManager.addTask(task);

        // Удаляем задачу
        taskManager.deleteTask(taskId);

        // Проверяем, что задача удалена
        assertNull(taskManager.getTask(taskId), "Задача не удалена.");
    }

    @Test
    void updateTask() {
        Task task = new Task(0, "Test Task for Update", "Test Task for Update description", TaskStatus.NEW);
        int taskId = taskManager.addTask(task);

        // Обновляем задачу
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);

        // Извлекаем обновлённую задачу
        Task updatedTask = taskManager.getTask(taskId);

        // Проверяем, что статус обновился
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus(), "Статус задачи не обновился.");
    }
}
