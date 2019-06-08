package com.onudapps.proman.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardWithUpdate;

import java.util.List;

public class BoardCardsViewModel extends ViewModel {
    private LiveData<List<BoardWithUpdate>> boardsData;

    public LiveData<List<BoardWithUpdate>> getBoardsData() {
        if (boardsData == null) {
            boardsData = Repository.REPOSITORY.getBoardCards();
        }
        return boardsData;
    }

    public void forceBoardsUpdate() {
        Repository.REPOSITORY.updateBoardCards();
    }
}
