package com.onudapps.proman.data.pojo;

import androidx.room.Embedded;
import com.onudapps.proman.data.db.entities.TaskDBEntity;

import java.util.Calendar;

public class Task {
    @Embedded
    private TaskDBEntity taskDBEntity;
    private String groupTitle;
    private Calendar updated;

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

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }
}
