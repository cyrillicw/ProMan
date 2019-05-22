package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "groups", foreignKeys = {
        @ForeignKey(entity = BoardDBEntity.class, parentColumns = "boardId", childColumns = "boardId", onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "boardId")})
public class GroupDBEntity {
    @PrimaryKey
    @NonNull
    private Integer groupId;
    private String title;
    private Integer boardId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }
}
