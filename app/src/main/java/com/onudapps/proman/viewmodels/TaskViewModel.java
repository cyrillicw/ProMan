package com.onudapps.proman.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.Task;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {
    private MutableLiveData<Task> data;
    private UUID id;
    private ExecutorService executorService;

    public TaskViewModel(@NonNull Application app, UUID id) {
        super(app);
        this.id = id;
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Task> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    public void loadData() {
        executorService.execute(() -> {
            Task task = Repository.REPOSITORY.getTask(id);
            data.postValue(task);
        });
    }

    public static class TaskModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final UUID id;
        private final Application app;

        public TaskModelFactory(@NonNull Application app, UUID id) {
            super();
            this.id = id;
            this.app = app;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == TaskViewModel.class) {
                return (T) new TaskViewModel(app, id);
            }
            return null;
        }
    }
}
