package com.onudapps.proman.pojo;

import java.util.List;

public class BoardGroup {
    private String title;
    private List<Task> tasks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
