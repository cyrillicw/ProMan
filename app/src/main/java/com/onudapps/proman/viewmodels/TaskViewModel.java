package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.TaskDBEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends ViewModel {
    private LiveData<TaskDBEntity> data;
    private int taskId;
    private ExecutorService executorService;

    public TaskViewModel(int taskId) {
        this.taskId = taskId;
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<TaskDBEntity> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    public void loadData() {
        data = Repository.REPOSITORY.getTaskDBEntity(taskId);
    }

    public static class TaskModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int id;

        public TaskModelFactory(int id) {
            super();
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == TaskViewModel.class) {
                return (T) new TaskViewModel(id);
            }
            return null;
        }
    }
}
