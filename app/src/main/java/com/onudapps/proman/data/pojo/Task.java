package com.onudapps.proman.data.pojo;

import androidx.room.Ignore;
import com.onudapps.TaskChange;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Task {
    private Integer taskId;
    private String title;
    private String description;
    @Ignore
    private List<ParticipantDBEntity> participants;
    private Calendar start;
    private Calendar finish;
    private String boardTitle;
    private String groupTitle;
    private Integer boardId;
    private Integer groupId;
    @Ignore
    private TaskChange taskChange;

    public Task(){}

    public Task (Task task) {
        title = task.getTitle();
        description = task.getDescription();
        participants = new ArrayList<>(task.getParticipants());
        start = Calendar.getInstance();
        start.setTimeInMillis(task.getStart().getTimeInMillis());
        finish = Calendar.getInstance();
        finish.setTimeInMillis(task.getFinish().getTimeInMillis());
        taskChange = null;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (taskChange != null) {
            taskChange.updateDescription(title);
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if (taskChange != null) {
            taskChange.updateDescription(description);
        }
    }

    public List<ParticipantDBEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDBEntity> participants) {
        this.participants = participants;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        if (start == null) {
            this.start = null;
        }
        else {
            if (this.start == null) {
                this.start = Calendar.getInstance();
            }
            this.start.setTimeInMillis(start.getTimeInMillis());
        }
        if (taskChange != null) {
            taskChange.updateStart(start);
        }
    }

    public Calendar getFinish() {
        return finish;
    }

    public void setFinish(Calendar finish) {
        if (finish == null) {
            this.finish = null;
        }
        else {
            if (this.finish == null) {
                this.finish = Calendar.getInstance();
            }
            this.finish.setTimeInMillis(finish.getTimeInMillis());
        }
        if (taskChange != null) {
            taskChange.updateFinish(finish);
        }
    }

    public TaskChange getTaskChange() {
        return taskChange;
    }

    public void setTaskChange(TaskChange taskChange) {
        this.taskChange = taskChange;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
