package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.TaskCard;

import java.util.List;

public class GroupViewModel extends ViewModel {

    private int id;
    private LiveData<String> titleData;
    private LiveData<List<TaskCard>> taskCardsData;

    private GroupViewModel(int id) {
        this.id = id;
    }

    public LiveData<String> getTitleData() {
        if (titleData == null) {
            titleData = Repository.REPOSITORY.getGroupTitle(id);
        }
        return titleData;
    }

    public LiveData<List<TaskCard>> getTaskCardData() {
        if (taskCardsData == null) {
            taskCardsData = Repository.REPOSITORY.getTaskCards(id);
        }
        return taskCardsData;
    }

    public static class GroupModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int id;

        public GroupModelFactory(int id) {
            super();
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == GroupViewModel.class) {
                return (T) new GroupViewModel(id);
            }
            return null;
        }
    }
}
