package com.onudapps.proman.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.LastUpdateEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.data.pojo.Task;
import com.onudapps.proman.data.pojo.TaskCard;
import org.web3j.tuples.generated.Tuple2;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

class LocalDataSource {
    private static final String databaseTitle = "ProManDatabase";
    private ProManDatabase database;
    LocalDataSource(Context context) {
        database = Room.databaseBuilder(context, ProManDatabase.class, databaseTitle).fallbackToDestructiveMigration().build();
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

    public void insertBoard(BoardDBEntity boardDBEntity) {
        database.getProManDao().insertBoard(boardDBEntity);
    }

    public LiveData<List<GroupDBEntity>> getBoardGroups(int boardId) {
        return database.getProManDao().getBoardGroups(boardId);
    }

    public LiveData<List<TaskCard>> getTaskCards(int groupId) {
        return database.getProManDao().getTaskCards(groupId);
    }

    public LiveData<String> getGroupTitle(int groupId) {
        return database.getProManDao().getGroupTitle(groupId);
    }

    public void insertGroup(GroupDBEntity groupDBEntity) {
        database.getProManDao().insertGroup(groupDBEntity);
    }

    public void removeBoard(int id) {
        database.getProManDao().removeBoard(id);
    }

    public void updateBoardCards(List<BoardDBEntity> boardDBEntities) {
        database.getProManDao().updateBoards(boardDBEntities);
    }

    public void updateBoard(BoardDBEntity boardDBEntity, List<Tuple2<GroupDBEntity, List<TaskDBEntity>>> groups) {
        database.getProManDao().updateBoard(boardDBEntity, groups);
    }

    public LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id) {
        return database.getProManDao().getLastUpdate(queryType, id);
    }
}
