package com.onudapps.proman.data.pojo;

import java.util.Calendar;

public class TaskCalendarCard {
    private String title;
    private Calendar start;
    private Calendar finish;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
