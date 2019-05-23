package com.onudapps.proman.data.pojo;

import androidx.room.Embedded;
import com.onudapps.proman.data.db.entities.BoardDBEntity;

import java.util.Calendar;

public class BoardWithUpdate {
    @Embedded
    BoardDBEntity boardDBEntity;
    Calendar updated;

    public BoardDBEntity getBoardDBEntity() {
        return boardDBEntity;
    }

    public void setBoardDBEntity(BoardDBEntity boardDBEntity) {
        this.boardDBEntity = boardDBEntity;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }
}
