package com.onudapps.proman.data.pojo;

import com.onudapps.proman.data.db.entities.GroupDBEntity;

import java.util.List;

public class BoardGroup {
    private GroupDBEntity groupDBEntity;
    private List<TaskDBEntityWithParticipantsAddresses> tasks;

    public GroupDBEntity getGroupDBEntity() {
        return groupDBEntity;
    }

    public void setGroupDBEntity(GroupDBEntity groupDBEntity) {
        this.groupDBEntity = groupDBEntity;
    }

    public List<TaskDBEntityWithParticipantsAddresses> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDBEntityWithParticipantsAddresses> tasks) {
        this.tasks = tasks;
    }
}
