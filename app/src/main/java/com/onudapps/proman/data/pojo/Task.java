package com.onudapps.proman.data.pojo;

import androidx.room.Embedded;
import com.onudapps.proman.data.db.entities.TaskDBEntity;

public class Task {
    @Embedded
    private TaskDBEntity taskDBEntity;
    private String groupTitle;

    public TaskDBEntity getTaskDBEntity() {
        return taskDBEntity;
    }

    public void setTaskDBEntity(TaskDBEntity taskDBEntity) {
        this.taskDBEntity = taskDBEntity;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
