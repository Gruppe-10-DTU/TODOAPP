package com.gruppe11.todoApp.model;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private Priority priority;
    private LocalDateTime completion;
    private boolean completed;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCompletion() {
        return completion;
    }

    public void setCompletion(LocalDateTime completion) {
        this.completion = completion;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
