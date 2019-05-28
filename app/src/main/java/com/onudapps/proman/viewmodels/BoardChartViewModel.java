package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.GroupStatistic;
import com.onudapps.proman.data.pojo.TaskCalendarCard;

import java.util.List;

public class BoardChartViewModel extends ViewModel{
    private int boardId;
    private LiveData<List<GroupStatistic>> groupsData;
    private LiveData<List<TaskCalendarCard>> tasksCalendarData;

    private BoardChartViewModel(int boardId) {
        this.boardId = boardId;
    }

    public  LiveData<List<GroupStatistic>> getGroupsData() {
        if (groupsData == null) {
            groupsData = Repository.REPOSITORY.getGroupsStatistics(boardId);
        }
        return groupsData;
    }

    public  LiveData<List<TaskCalendarCard>> getTasksCalendarData() {
        if (tasksCalendarData == null) {
            tasksCalendarData = Repository.REPOSITORY.getTasksCalendarData(boardId);
        }
        return tasksCalendarData;
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
