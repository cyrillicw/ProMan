package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "task_participant_join", primaryKeys = {"taskId", "participantPublicKey"},
        foreignKeys = {
            @ForeignKey(entity = TaskDBEntity.class, parentColumns = "taskId", childColumns = "taskId", onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = ParticipantDBEntity.class, parentColumns = "publicKey", childColumns = "participantPublicKey", onDelete = ForeignKey.CASCADE)
        }, indices = {@Index(value = "taskId"), @Index(value = "participantPublicKey")})
public class TaskParticipantJoin {
    @NonNull
    private Integer taskId;
    @NonNull
    private String participantPublicKey;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getParticipantPublicKey() {
        return participantPublicKey;
    }

    public void setParticipantPublicKey(String participantPublicKey) {
        this.participantPublicKey = participantPublicKey;
    }
}
