package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;

public class TaskChangeGroupViewModel extends ViewModel{
    private String[] groupsTitles;
    private int[] groupsIds;
    private int taskId;

    private TaskChangeGroupViewModel(String[] groupsTitles, int[] groupsIds, int taskId) {
        this.groupsTitles = groupsTitles;
        this.groupsIds = groupsIds;
        this.taskId = taskId;
    }

    public void moveTaskToGroup(int taskId, int groupIndex) {
        Repository.REPOSITORY.moveTaskToGroup(taskId, groupsIds[groupIndex]);
    }

    public String[] getGroupsTitles() {
        return groupsTitles;
    }

    public void setGroupsTitles(String[] groupsTitles) {
        this.groupsTitles = groupsTitles;
    }

    public static class TaskChangeGroupModelFactory extends ViewModelProvider.NewInstanceFactory {
        private String[] groupsTitles;
        private int[] groupsIds;
        private int taskId;

        public TaskChangeGroupModelFactory(String[] groupsTitles, int[] groupsIds, int taskId) {
            super();
            this.groupsTitles = groupsTitles;
            this.groupsIds = groupsIds;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == TaskChangeGroupViewModel.class) {
                return (T) new TaskChangeGroupViewModel(groupsTitles, groupsIds, taskId);
            }
            return null;
        }
    }
}
