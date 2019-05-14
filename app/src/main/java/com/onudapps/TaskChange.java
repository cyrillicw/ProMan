package com.onudapps;

import com.onudapps.proman.data.entities.Task;

import java.util.Calendar;
import java.util.HashSet;

public class TaskChange {
    private static final int PROPERTIES_QUANTITY = 6;
    private static final String LOG_TAG = "TaskChange";

    private Task original;

    private String title;
    private String description;
    private HashSet<String> addedParticipants;
    private HashSet<String> deletedParticipants;
    private Calendar start;
    private Calendar finish;

    private boolean[] changed;

    public TaskChange(Task original) {
        this.original = original;
        changed = new boolean[PROPERTIES_QUANTITY];
        for (int i = 0; i < changed.length; i++) {
            changed[i] = false;
        }
        title = null;
        description = null;
        addedParticipants = new HashSet<>();
        deletedParticipants = new HashSet<>();
        start = null;
        finish = null;
    }

    public void updateTitle(String title) {
        if (!original.getTitle().equals(title)) {
            this.title = title;
            changed[0] = true;
        }
        else {
            changed[0] = false;
            this.title = null;
        }
    }

    public void updateDescription(String description) {
        if (!original.getDescription().equals(description)) {
            this.description = description;
            changed[1] = true;
        }
        else {
            changed[1] = false;
            this.description = null;
        }
    }

    public void addParticipant(String participant) {
        int add = 0;
        if (deletedParticipants.contains(participant)) {
            deletedParticipants.remove(participant);
            if (deletedParticipants.size() == 0) {
                changed[3] = false;
            }
        }
        else {
            addedParticipants.add(participant);
            changed[2] = true;
        }
    }

    public void removeParticipant(String participant) {
        int add = 0;
        if (addedParticipants.contains(participant)) {
            addedParticipants.remove(participant);
            if (addedParticipants.size() == 0) {
                changed[2] = false;
            }
        }
        else {
            addedParticipants.add(participant);
            changed[3] = true;
        }
    }

    public void updateStart(Calendar start) {
        if (original.getStart() == null) {
            if (start == null) {
                changed[4] = false;
            }
            else {
                if (this.start == null) {
                    this.start = Calendar.getInstance();
                }
                this.start.setTimeInMillis(start.getTimeInMillis());
                changed[4] = true;
            }
        }
        else {
            if (!original.getStart().equals(start)) {
                if (start == null) {
                    this.start = null;
                }
                else {
                    if (this.start == null) {
                        this.start = Calendar.getInstance();
                    }
                    this.start.setTimeInMillis(start.getTimeInMillis());
                }
                changed[4] = true;
            }
            else {
                changed[4] = false;
            }
        }
    }

    public void updateFinish(Calendar finish) {
        if (original.getFinish() == null) {
            if (finish == null) {
                changed[5] = false;
            }
            else {
                if (this.finish == null) {
                    this.finish = Calendar.getInstance();
                }
                this.finish.setTimeInMillis(finish.getTimeInMillis());
                changed[5] = true;
            }
        }
        else {
            if (!original.getFinish().equals(finish)) {
                if (finish == null) {
                    this.finish = null;
                }
                else {
                    if (this.finish == null) {
                        this.finish = Calendar.getInstance();
                    }
                    this.finish.setTimeInMillis(finish.getTimeInMillis());
                }
                changed[5] = true;
            }
            else {
                changed[5] = false;
            }
        }
    }

    public boolean changesDetected() {
        boolean res = false;
        for (boolean e: changed) {
            res |= e;
        }
        return res;
    }
}
