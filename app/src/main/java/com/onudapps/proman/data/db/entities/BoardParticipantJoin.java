package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;

@Entity(tableName = "board_participant_join", primaryKeys = {"boardId", "address"},
        foreignKeys = {
                @ForeignKey(entity = BoardDBEntity.class, parentColumns = "boardId", childColumns = "boardId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = ParticipantDBEntity.class, parentColumns = "address", childColumns = "address", onDelete = ForeignKey.CASCADE)
        }, indices = {@Index(value = "boardId"), @Index(value = "address")})
public class BoardParticipantJoin {
    @NonNull
    private Integer boardId;
    @NonNull
    private String address;

    @NonNull
    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(@NonNull Integer boardId) {
        this.boardId = boardId;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }
}
