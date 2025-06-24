package controllers;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    // Добавляет задачу в историю просмотров
    void add(Task task);

    void remove(int id);

    // Возвращает список задач из истории просмотров
    ArrayList<Task> getHistory();
}