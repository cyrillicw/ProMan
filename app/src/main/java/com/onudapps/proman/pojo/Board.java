package com.onudapps.proman.pojo;

import java.util.Date;
import java.util.List;

public class Board {
    private String id;
    private String title;
    private Date startDate;
    private Date finishDate;
    private String[] participants;
    private List<BoardGroup> boardGroups;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String[] getParticipants() {
        return participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BoardGroup> getBoardGroups() {
        return boardGroups;
    }

    public void setBoardGroups(List<BoardGroup> boardGroups) {
        this.boardGroups = boardGroups;
    }
}
