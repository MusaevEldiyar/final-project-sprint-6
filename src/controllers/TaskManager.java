package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    // Методы для управления задачами, эпиками и подзадачами
    int addTask(Task task);
    Task getTask(int id);
    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();
    void updateTask(Task task);

    void deleteTask(int id);

    // Методы для работы с эпиками и подзадачами
    ArrayList<Subtask> getEpicSubtasks(int epicId);

    // Метод для работы с историей
    ArrayList<Task> getHistory();

    //Удаление всех задач
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();
}
