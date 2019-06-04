package com.onudapps.proman.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.TaskCard;

import java.util.List;

public class CurrentUserTasksViewModel extends ViewModel {
    private LiveData<List<TaskCard>> tasksData;

    public LiveData<List<TaskCard>> getTasksData() {
        if (tasksData == null) {
            Repository.REPOSITORY.getCurrentUserTaskCards();
        }
        return tasksData;
    }
}
