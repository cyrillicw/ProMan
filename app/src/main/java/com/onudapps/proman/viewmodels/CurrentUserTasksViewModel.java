package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.TaskCard;

import java.util.List;

public class CurrentUserTasksViewModel extends ViewModel {
    private LiveData<List<TaskCard>> tasksData;
    private int boardId;

    private CurrentUserTasksViewModel(int boardId) {
        this.boardId = boardId;
    }

    public LiveData<List<TaskCard>> getTasksData() {
        if (tasksData == null) {
            tasksData = Repository.REPOSITORY.getCurrentUserTaskCards(boardId);
        }
        return tasksData;
    }

    public static class CurrentUserTasksModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int boardId;

        public CurrentUserTasksModelFactory(int boardId) {
            super();
            this.boardId = boardId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == CurrentUserTasksViewModel.class) {
                return (T) new CurrentUserTasksViewModel(boardId);
            }
            return null;
        }
    }
}
