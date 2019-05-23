package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.GroupWithUpdate;

import java.util.List;

public class BoardChartViewModel extends ViewModel{
    private int id;
    private LiveData<List<GroupWithUpdate>> groupsData;

    private BoardChartViewModel(int id) {
        this.id = id;
    }

    public  LiveData<List<GroupWithUpdate>> getGroupsData() {
        if (groupsData == null) {
            groupsData = Repository.REPOSITORY.getBoardGroups(id);
        }
        return groupsData;
    }

    public static class BoardChartModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int id;

        public BoardChartModelFactory(int id) {
            super();
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == BoardChartViewModel.class) {
                return (T) new BoardChartViewModel(id);
            }
            return null;
        }
    }
}
