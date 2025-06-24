package util;

import controllers.HistoryManager;
import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.TaskManager;

public class Managers {

    // Метод для получения стандартной реализации TaskManager
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Метод для получения стандартной реализации HistoryManager
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
