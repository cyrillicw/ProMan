package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.StartFinishDates;

import java.util.Calendar;

public class BoardPropertiesViewModel extends ViewModel{
    private int boardId;
    private LiveData<StartFinishDates> startFinishData;

    private BoardPropertiesViewModel(int boardId) {
        this.boardId = boardId;
    }

    public LiveData<StartFinishDates> getDatesData() {
        if (startFinishData == null) {
            startFinishData = Repository.REPOSITORY.getBoardStartFinishDates(boardId);
        }
        return startFinishData;
    }

    public void updateStart(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.SECOND, 0);
        }
        Repository.REPOSITORY.setBoardStart(boardId, calendar);
    }

    public void updateFinish(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.SECOND, 0);
        }
        Repository.REPOSITORY.setBoardFinish(boardId, calendar);
    }

    public static class BoardPropertiesModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int boardId;

        public BoardPropertiesModelFactory(int boardId) {
            super();
            this.boardId = boardId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == BoardPropertiesViewModel.class) {
                return (T) new BoardPropertiesViewModel(boardId);
            }
            return null;
        }
    }
}
