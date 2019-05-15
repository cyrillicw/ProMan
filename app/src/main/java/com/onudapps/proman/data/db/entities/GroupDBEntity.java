package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "groups", foreignKeys = {
        @ForeignKey(entity = BoardDBEntity.class, parentColumns = "boardId", childColumns = "boardId")},
        indices = {@Index(value = "boardId")})
public class GroupDBEntity {
    @PrimaryKey
    @NonNull
    private UUID groupId;
    private String title;
    private UUID boardId;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getBoardId() {
        return boardId;
    }

    public void setBoardId(UUID boardId) {
        this.boardId = boardId;
    }
}
