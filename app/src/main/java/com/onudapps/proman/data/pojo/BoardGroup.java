package com.onudapps.proman.data.pojo;

import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;

import java.util.List;

public class BoardGroup {
    private GroupDBEntity groupDBEntity;
    private List<TaskDBEntity> tasks;

    public GroupDBEntity getGroupDBEntity() {
        return groupDBEntity;
    }

    public void setGroupDBEntity(GroupDBEntity groupDBEntity) {
        this.groupDBEntity = groupDBEntity;
    }

    public List<TaskDBEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDBEntity> tasks) {
        this.tasks = tasks;
    }
}
