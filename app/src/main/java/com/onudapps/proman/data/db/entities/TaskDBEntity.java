package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.UUID;

@Entity(tableName = "tasks", foreignKeys = {
        @ForeignKey(entity = BoardDBEntity.class, parentColumns = "boardId", childColumns = "boardId"),
        @ForeignKey(entity = GroupDBEntity.class, parentColumns = "groupId", childColumns = "groupId")},
        indices = {@Index(value = "boardId"), @Index(value = "groupId")})
public class TaskDBEntity {
    @PrimaryKey
    @NonNull
    private UUID taskId;
    private String title;
    private String description;
    private Calendar start;
    private Calendar finish;
    private String boardId;
    private String groupId;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
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

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
