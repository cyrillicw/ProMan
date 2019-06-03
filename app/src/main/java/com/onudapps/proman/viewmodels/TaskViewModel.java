package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.GroupShortInfo;
import com.onudapps.proman.data.pojo.Task;

import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends ViewModel{
    private LiveData<Task> taskData;
    private LiveData<List<GroupShortInfo>> groupsData;
    private int taskId;
    private int boardId;

    public TaskViewModel(int taskId, int boardId) {
        this.taskId = taskId;
        this.boardId = boardId;
    }

    public void updateStart(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.SECOND, 0);
        }
        Repository.REPOSITORY.setTaskStart(taskId, calendar);
    }

    public void updateFinish(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.SECOND, 0);
        }
        Repository.REPOSITORY.setTaskFinish(taskId, calendar);
    }

    public LiveData<Task> getTaskData() {
        if (taskData == null) {
            taskData = Repository.REPOSITORY.getTask(taskId);
        }
        return taskData;
    }

    public LiveData<List<GroupShortInfo>> getGroupsData() {
        if (groupsData == null) {
            groupsData = Repository.REPOSITORY.getGroupsShortInfo(boardId);
        }
        return groupsData;
    }

    public void updateDescription(String description) {
        Repository.REPOSITORY.updateTaskDescription(taskId, description);
    }

    public void updateTitle(String title) {
        Repository.REPOSITORY.updateTaskTitle(taskId, title);
    }

    public static class TaskModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int taskId;
        private final int boardId;

        public TaskModelFactory(int taskId, int boardId) {
            super();
            this.taskId = taskId;
            this.boardId = boardId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == TaskViewModel.class) {
                return (T) new TaskViewModel(taskId, boardId);
            }
            return null;
        }
    }
}
