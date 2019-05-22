package com.onudapps.proman.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.LastUpdateEntity;
import com.onudapps.proman.data.pojo.BoardCard;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class BoardCardsViewModel extends ViewModel {
    private LiveData<List<BoardCard>> boardsData;
    private LiveData<Calendar> lastUpdateData;
    private UUID id;
    private ExecutorService executorService;

    public LiveData<List<BoardCard>> getBoardsData() {
        if (boardsData == null) {
            boardsData = Repository.REPOSITORY.getBoardCards();
        }
        return boardsData;
    }

    public LiveData<Calendar> getLastUpdateData() {
        if (lastUpdateData == null) {
            lastUpdateData = Repository.REPOSITORY.getLastUpdate(LastUpdateEntity.Query.BOARDS, -1);
        }
        return lastUpdateData;
    }

    public void forceBoardsUpdate() {
        Repository.REPOSITORY.updateBoardCards();
    }

    public void createBoard(String title) {
        Repository.REPOSITORY.createBoard(title);
    }

    public void leaveBoard(int id) {
        Repository.REPOSITORY.leaveBoard(id);
    }
}
