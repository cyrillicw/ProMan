package com.onudapps.proman.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardWithUpdate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class BoardCardsViewModel extends ViewModel {
    private LiveData<List<BoardWithUpdate>> boardsData;
    private UUID id;
    private ExecutorService executorService;

    public LiveData<List<BoardWithUpdate>> getBoardsData() {
        if (boardsData == null) {
            boardsData = Repository.REPOSITORY.getBoardCards();
        }
        return boardsData;
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
