package com.onudapps.proman.data.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Task {
    private String title;
    private String description;
    private List<String> participants;
    private Calendar start;
    private Calendar finish;

    public Task(){}

    public Task (Task task) {
        title = task.getTitle();
        description = task.getDescription();
        participants = new ArrayList<>(task.getParticipants());
        start = Calendar.getInstance();
        start.setTimeInMillis(task.getStart().getTimeInMillis());
        finish = Calendar.getInstance();
        finish.setTimeInMillis(task.getFinish().getTimeInMillis());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getFinish() {
        return finish;
    }

    public void setFinish(Calendar finish) {
        this.finish = finish;
    }
}
