package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
    }

    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtaskIds); // Возвращаем копию списка
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId)); // Удаляем по значению, а не индексу
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
