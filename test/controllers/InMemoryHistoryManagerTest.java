package controllers;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import util.Managers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    @Test
    void addAndGetHistorySingleTask() {
        Task task = new Task(1, "Test Task 1", "", TaskStatus.NEW);
        historyManager.add(task);

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void addTaskToHistory() {
        Task task = new Task(1, "Test Task", "Test Task Description", TaskStatus.NEW);
        historyManager.add(task);
        ArrayList<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "Неверный размер истории.");
    }


    @Test
    void addAndGetHistoryMultipleTasksOrderIsPreserved() {
        Task task1 = new Task(1, "Task 1", "Test Task Description", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Test Task Description", TaskStatus.IN_PROGRESS);
        Task task3 = new Task(3, "Task 3", "Test Task Description", TaskStatus.DONE);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    void addDuplicateTaskMovesTaskToEnd() {
        Task task1 = new Task(1, "Task 1", "Test Task Description", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Test Task Description", TaskStatus.IN_PROGRESS);
        Task task3 = new Task(3, "Task 3", "Test Task Description", TaskStatus.DONE);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2);

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task2, history.get(2));
    }

    @Test
    void removeTaskRemovesFromHistory() {
        Task task1 = new Task(1, "Task 1", "Test Task Description", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Test Task Description", TaskStatus.IN_PROGRESS);
        Task task3 = new Task(3, "Task 3", "Test Task Description", TaskStatus.DONE);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertFalse(history.contains(task2));
    }
    @Test
    void addNullTask() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty());
    }

}
