package com.onudapps.proman.data.pojo;

import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;

import java.util.List;

public class Board {
    private BoardDBEntity boardDBEntity;
    private List<ParticipantDBEntity> participants;
    private List<BoardGroup> boardGroups;

    public BoardDBEntity getBoardDBEntity() {
        return boardDBEntity;
    }

    public void setBoardDBEntity(BoardDBEntity boardDBEntity) {
        this.boardDBEntity = boardDBEntity;
    }

    public List<ParticipantDBEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDBEntity> participants) {
        this.participants = participants;
    }

    public List<BoardGroup> getBoardGroups() {
        return boardGroups;
    }

    public void setBoardGroups(List<BoardGroup> boardGroups) {
        this.boardGroups = boardGroups;
    }
}

