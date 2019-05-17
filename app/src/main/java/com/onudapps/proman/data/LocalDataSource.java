package com.onudapps.proman.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.data.pojo.Task;

import java.util.List;
import java.util.UUID;

class LocalDataSource {
    private static final String databaseTitle = "ProManDatabase";
    private ProManDatabase database;
    LocalDataSource(Context context) {
        database = Room.databaseBuilder(context, ProManDatabase.class, databaseTitle).build();
    }

    public Task getTask(UUID id) {
        return database.getProManDao().getTask(id);
    }

    public void insertTask(Task task) {
        database.getProManDao().insertTask(task);
    }

    public LiveData<List<BoardCard>> getBoardCards() {
        return database.getProManDao().getBoardCards();
    }

    public void insertBoardCard(BoardCard boardCard) {
        BoardDBEntity boardDBEntity = new BoardDBEntity();
        boardDBEntity.setBoardId(boardCard.getBoardId());
        boardDBEntity.setTitle(boardCard.getTitle());
        boardDBEntity.setStart(boardCard.getStart());
        boardDBEntity.setFinish(boardCard.getFinish());
    }
}
