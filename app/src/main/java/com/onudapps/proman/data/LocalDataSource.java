package com.onudapps.proman.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.onudapps.proman.data.db.ProManDatabase;
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

    LiveData<TaskDBEntity> getTaskDBEntity(int taskId) {
        return database.getProManDao().getTaskDBEntity(taskId);
    }

    void onSignOut() {
        database.getProManDao().clearData();
    }

    void removeBoardParticipant(int boardId, String address) {
        database.getProManDao().removeBoardParticipant(boardId, address);
    }

    LiveData<List<ParticipantDBEntity>> getBoardParticipants(int boardId) {
        return database.getProManDao().getBoardParticipants(boardId);
    }

    LiveData<List<ParticipantDBEntity>> getTaskParticipants(int taskId) {
        return database.getProManDao().getTaskParticipants(taskId);
    }

    LiveData<Task> getTask(int taskId) {
        return database.getProManDao().getTask(taskId);
    }

    void addTaskParticipant(int taskId, ParticipantDBEntity participantDBEntity) {
        database.getProManDao().addTaskParticipant(taskId, participantDBEntity);
    }

    void insertTaskDBEntity(TaskDBEntity taskDBEntity) {
        database.getProManDao().insertTaskDBEntity(taskDBEntity);
    }

    void insertTaskWithParticipants(TaskDBEntityWithParticipants entity) {
        database.getProManDao().insertTaskWithParticipants(entity);
    }

    LiveData<List<TaskCalendarCard>> getTasksCalendarData(int boardId) {
        return database.getProManDao().getTasksCalendarCard(boardId);
    }

    LiveData<List<GroupShortInfo>> getGroupsShortInfo(int boardId) {
        return database.getProManDao().getGroupsShortInfo(boardId);
    }

    LiveData<StartFinishDates> getBoardStartFinishDates(int boardId) {
        return database.getProManDao().getBoardStartFinishDates(boardId);
    }

    void removeTaskParticipant(int taskId, String address) {
        database.getProManDao().removeTaskParticipant(taskId, address);
    }

    LiveData<List<BoardWithUpdate>> getBoardCards() {
        return database.getProManDao().getBoardCards();
    }

    void insertBoard(BoardDBEntity boardDBEntity) {
        database.getProManDao().insertBoard(boardDBEntity);
    }

    LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId) {
        return database.getProManDao().getBoardGroups(boardId);
    }

    LiveData<List<TaskCard>> getUserTaskCards(int boardId, String address) {
        return database.getProManDao().getUserTaskCards(boardId, address);
    }

    void setTaskGroup(int taskId, int groupId) {
        database.getProManDao().setTaskGroup(taskId, groupId);
    }

    void setTaskDescription(int taskId, String description) {
        database.getProManDao().setTaskDescription(taskId, description);
    }

    void setTaskTitle(int taskId, String title) {
        database.getProManDao().setTaskTitle(taskId, title);
    }

    void addBoardParticipant(int boardId, ParticipantDBEntity participantDBEntity) {
        database.getProManDao().addBoardParticipant(boardId, participantDBEntity);
    }

    LiveData<List<TaskCard>> getTaskCards(int groupId) {
        return database.getProManDao().getTaskCards(groupId);
    }

    LiveData<String> getGroupTitle(int groupId) {
        return database.getProManDao().getGroupTitle(groupId);
    }

    void insertGroup(GroupDBEntity groupDBEntity) {
        database.getProManDao().insertGroup(groupDBEntity);
    }

    void removeBoard(int id) {
        database.getProManDao().removeBoard(id);
    }

    void updateBoardCards(List<BoardDBEntity> boardDBEntities) {
        database.getProManDao().updateBoards(boardDBEntities);
    }

    LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId) {
        return database.getProManDao().getGroupsStatistics(boardId);
    }

    LiveData<String> getBoardTitle(int id) {
        return database.getProManDao().getBoardTitle(id);
    }

    void updateBoard(Board board) {
        database.getProManDao().updateBoard(board);
    }

    LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id) {
        return database.getProManDao().getLastUpdate(queryType, id);
    }

    void setTaskStart(int taskId, Calendar calendar) {
        database.getProManDao().setTaskStart(taskId, calendar);
    }

    void setTaskFinish(int taskId, Calendar calendar) {
        database.getProManDao().setTaskFinish(taskId, calendar);
    }

    void setBoardStart(int boardId, Calendar calendar) {
        database.getProManDao().setBoardStart(boardId, calendar);
    }

    void setBoardFinish(int boardId, Calendar calendar) {
        database.getProManDao().setBoardFinish(boardId, calendar);
    }
}
