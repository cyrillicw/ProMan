package com.onudapps.proman.data.pojo;

import androidx.room.Embedded;
import com.onudapps.proman.data.db.entities.GroupDBEntity;

import java.util.Calendar;

public class GroupWithUpdate {
    @Embedded
    private GroupDBEntity groupDBEntity;
    private Calendar updated;

    public GroupDBEntity getGroupDBEntity() {
        return groupDBEntity;
    }

    public void setGroupDBEntity(GroupDBEntity groupDBEntity) {
        this.groupDBEntity = groupDBEntity;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }
}
