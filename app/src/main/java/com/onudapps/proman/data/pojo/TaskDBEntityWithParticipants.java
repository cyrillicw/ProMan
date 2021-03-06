package com.onudapps.proman.data.pojo;

import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;

import java.util.List;

public class TaskDBEntityWithParticipants {
    private TaskDBEntity taskDBEntity;
    private List<ParticipantDBEntity> participants;

    public TaskDBEntity getTaskDBEntity() {
        return taskDBEntity;
    }

    public void setTaskDBEntity(TaskDBEntity taskDBEntity) {
        this.taskDBEntity = taskDBEntity;
    }

    public List<ParticipantDBEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDBEntity> participants) {
        this.participants = participants;
    }
}
