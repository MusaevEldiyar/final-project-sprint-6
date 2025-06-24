package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 0;

    // Генерация уникального ID
    private int generateId() {
        return ++idCounter;
    }

    // Универсальный метод для добавления задач, эпиков и подзадач
    @Override
    public int addTask(Task task) {
        int id = generateId();
        task.setId(id);
        // Различаем тип задачи и добавляем в соответствующую коллекцию
        if (task instanceof Epic) {
            epics.put(id, (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(id, subtask);

            // Получаем эпик, к которому относится подзадача
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                // Добавляем подзадачу в эпик
                epic.addSubtask(id);
            }
        } else {
            tasks.put(id, task);
        }
        return id;
    }

    // Универсальный метод для получения задачи, эпика или подзадачи
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            task = epics.get(id);  // Проверяем среди эпиков
        }
        if (task == null) {
            task = subtasks.get(id);  // Проверяем среди подзадач
        }

        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());  // Возвращаем все задачи
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());  // Возвращаем все подзадачи
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());  // Возвращаем все эпики
    }

    // Универсальный метод для обновления задачи, эпика или подзадачи
    @Override
    public void updateTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);

            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        } else {
            tasks.put(task.getId(), task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }

        // Обновляем статус эпика
        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    // Универсальный метод для удаления задачи, эпика или подзадачи
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);  // Удаляем подзадачу

            if (subtask != null) {
                Epic epic = epics.get(subtask.getEpicId());

                if (epic != null) {
                    epic.removeSubtask(id);
                    updateEpicStatus(epic);
                }
            }
        }
    }

    // Методы для получения истории просмотров
    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return new ArrayList<>();
        }

        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasks()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }

        return epicSubtasks;
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }
}
