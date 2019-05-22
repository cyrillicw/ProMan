package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.LastUpdateEntity;

import java.util.Calendar;
import java.util.List;

public class BoardViewModel extends ViewModel {

    private int id;
    private LiveData<List<GroupDBEntity>> groupsData;
    private LiveData<Calendar> lastUpdateData;

    private BoardViewModel(int id) {
        this.id = id;
    }

    public  LiveData<List<GroupDBEntity>> getGroupsData() {
        if (groupsData == null) {
            groupsData = Repository.REPOSITORY.getBoardGroups(id);
        }
        return groupsData;
    }

    public LiveData<Calendar> getLastUpdateData() {
        if (lastUpdateData == null) {
            lastUpdateData = Repository.REPOSITORY.getLastUpdate(LastUpdateEntity.Query.BOARD, id);
        }
        return lastUpdateData;
    }

    public void forceBoardUpdate() {
        Repository.REPOSITORY.updateBoard(id);
    }

    public void createGroup(String title) {
        Repository.REPOSITORY.createGroup(title, id);
    }

    public static class BoardModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int id;

        public BoardModelFactory(int id) {
            super();
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == BoardViewModel.class) {
                return (T) new BoardViewModel(id);
            }
            return null;
        }
    }
}
