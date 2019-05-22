package com.onudapps.proman.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Calendar;

@Entity(tableName = "last_update", primaryKeys = {"queryType", "id"})
public class LastUpdateEntity {
    public enum Query {
        BOARDS, BOARD, TASK;
    }

    @NonNull
    private Query queryType;
    private int id;
    private Calendar updated;

    public Query getQueryType() {
        return queryType;
    }

    public void setQueryType(Query queryType) {
        this.queryType = queryType;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
