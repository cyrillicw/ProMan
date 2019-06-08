package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.TaskCard;

import java.util.List;

public class GroupViewModel extends ViewModel {

    private int groupId;
    private int boardId;
    private LiveData<String> titleData;
    private LiveData<List<TaskCard>> taskCardsData;
    private boolean editMode;
    private String taskTitle;

    private GroupViewModel(int groupId, int boardId) {
        this.groupId = groupId;
        this.boardId = boardId;
        editMode = false;
        taskTitle = "";
    }

    public LiveData<String> getTitleData() {
        if (titleData == null) {
            titleData = Repository.REPOSITORY.getGroupTitle(groupId);
        }
        return titleData;
    }

    public LiveData<List<TaskCard>> getTaskCardData() {
        if (taskCardsData == null) {
            taskCardsData = Repository.REPOSITORY.getTaskCards(groupId);
        }
        return taskCardsData;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setText(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void createTask(String title) {
        Repository.REPOSITORY.createTask(boardId, groupId, title);
    }

    public static class GroupModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int groupId;
        private final int boardId;

        public GroupModelFactory(int groupId, int boardId) {
            super();
            this.groupId = groupId;
            this.boardId = boardId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == GroupViewModel.class) {
                return (T) new GroupViewModel(groupId, boardId);
            }
            return null;
        }
    }
}
