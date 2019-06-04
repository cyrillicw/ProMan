package com.onudapps.proman.data.pojo;

import com.onudapps.proman.data.db.entities.TaskDBEntity;

import java.util.List;

public class TaskDBEntityWithParticipants {
    private TaskDBEntity taskDBEntity;
    private List<String> participants;

    public TaskDBEntity getTaskDBEntity() {
        return taskDBEntity;
    }

    public void setTaskDBEntity(TaskDBEntity taskDBEntity) {
        this.taskDBEntity = taskDBEntity;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
