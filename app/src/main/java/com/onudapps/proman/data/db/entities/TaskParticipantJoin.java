package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import java.util.UUID;

@Entity(tableName = "task_participant_join", primaryKeys = {"taskId", "participantPublicKey"},
        foreignKeys = {
            @ForeignKey(entity = TaskDBEntity.class, parentColumns = "taskId", childColumns = "taskId"),
            @ForeignKey(entity = ParticipantDBEntity.class, parentColumns = "publicKey", childColumns = "participantPublicKey")
        }, indices = {@Index(value = "taskId"), @Index(value = "participantPublicKey")})
public class TaskParticipantJoin {
    @NonNull
    private UUID taskId;
    @NonNull
    private String participantPublicKey;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getParticipantPublicKey() {
        return participantPublicKey;
    }

    public void setParticipantPublicKey(String participantPublicKey) {
        this.participantPublicKey = participantPublicKey;
    }
}
