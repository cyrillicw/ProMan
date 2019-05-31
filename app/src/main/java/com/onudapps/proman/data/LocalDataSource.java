package com.onudapps.proman.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.onudapps.proman.data.db.entities.*;
import com.onudapps.proman.data.pojo.*;

import java.util.Calendar;
import java.util.List;

class LocalDataSource {
    private static final String databaseTitle = "ProManDatabase";
    private ProManDatabase database;
    LocalDataSource(Context context) {
        database = Room.databaseBuilder(context, ProManDatabase.class, databaseTitle).fallbackToDestructiveMigration().build();
    }

    public LiveData<TaskDBEntity> getTaskDBEntity(int taskId) {
        return database.getProManDao().getTaskDBEntity(taskId);
    }

    public LiveData<List<ParticipantDBEntity>> getBoardParticipants(int boardId) {
        return database.getProManDao().getBoardParticipants(boardId);
    }

    public void insertTask(Task task) {
        database.getProManDao().insertTask(task);
    }

    public void insertTaskDBEntity(TaskDBEntity taskDBEntity) {
        database.getProManDao().insertTaskDBEntity(taskDBEntity);
    }

    public LiveData<List<TaskCalendarCard>> getTasksCalendarData(int boardId) {
        return database.getProManDao().getTasksCalendarCard(boardId);
    }
    public LiveData<StartFinishDates> getBoardStartFinishDates(int boardId) {
        return database.getProManDao().getBoardStartFinishDates(boardId);
    }

    public LiveData<List<BoardWithUpdate>> getBoardCards() {
        return database.getProManDao().getBoardCards();
    }

    public void insertBoard(BoardDBEntity boardDBEntity) {
        database.getProManDao().insertBoard(boardDBEntity);
    }

    public LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId) {
        return database.getProManDao().getBoardGroups(boardId);
    }

    public void addBoardParticipant(int boardId, ParticipantDBEntity participantDBEntity) {
        database.getProManDao().addBoardParticipant(boardId, participantDBEntity);
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

    public LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId) {
        return database.getProManDao().getGroupsStatistics(boardId);
    }

    public LiveData<String> getBoardTitle(int id) {
        return database.getProManDao().getBoardTitle(id);
    }

    public void updateBoard(Board board) {
        database.getProManDao().updateBoard(board);
    }

    public LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id) {
        return database.getProManDao().getLastUpdate(queryType, id);
    }

    public void setTaskStart(int taskId, Calendar calendar) {
        database.getProManDao().setTaskStart(taskId, calendar);
    }

    public void setTaskFinish(int taskId, Calendar calendar) {
        database.getProManDao().setTaskFinish(taskId, calendar);
    }

    public void setBoardStart(int boardId, Calendar calendar) {
        database.getProManDao().setBoardStart(boardId, calendar);
    }

    public void setBoardFinish(int boardId, Calendar calendar) {
        database.getProManDao().setBoardFinish(boardId, calendar);
    }
}
