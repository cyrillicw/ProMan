package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "task_participant_join", primaryKeys = {"taskId", "address"},
        foreignKeys = {
            @ForeignKey(entity = TaskDBEntity.class, parentColumns = "taskId", childColumns = "taskId", onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = ParticipantDBEntity.class, parentColumns = "address", childColumns = "address", onDelete = ForeignKey.CASCADE)
        }, indices = {@Index(value = "taskId"), @Index(value = "address")})
public class TaskParticipantJoin {
    @NonNull
    private Integer taskId;
    @NonNull
    private String address;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
