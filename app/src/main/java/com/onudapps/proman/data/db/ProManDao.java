package com.onudapps.proman.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.onudapps.proman.data.db.entities.*;
import com.onudapps.proman.data.pojo.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.onudapps.proman.data.db.entities.LastUpdateEntity.Query.TASK;

@Dao
public abstract class ProManDao {
    private static final String LOG_TAG = "ProManDao";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBoard(BoardDBEntity boardDBEntity);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGroup(GroupDBEntity groupDBEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(ParticipantDBEntity participantDBEntity);

    @Query("SELECT * FROM tasks WHERE tasks.taskId = :taskId")
    public abstract LiveData<TaskDBEntity> getTaskDBEntity(int taskId);

    @Query("SELECT tasks.*, groups.title as groupTitle, last_update.updated " +
            "FROM tasks " +
            "INNER JOIN groups ON tasks.groupId = groups.groupId " +
            "INNER JOIN last_update ON tasks.taskId = last_update.id AND last_update.queryType = 'TASK' " +
            "WHERE tasks.taskId = :taskId")
    public abstract LiveData<Task> getTask(int taskId);

    @Query("SELECT boardId FROM tasks WHERE taskId = :taskId")
    protected abstract int getBoardId(int taskId);

    @Transaction
    public void addBoardParticipant(int boardId, ParticipantDBEntity participantDBEntity) {
        insertParticipant(participantDBEntity);
        BoardParticipantJoin join = new BoardParticipantJoin();
        join.setBoardId(boardId);
        join.setAddress(participantDBEntity.getAddress());
        insertBoardParticipantJoin(join);
    }

    public void insertTaskWithParticipants(TaskDBEntityWithParticipants entity) {
        TaskDBEntity taskDBEntity = entity.getTaskDBEntity();
        insertTaskDBEntity(taskDBEntity);
        List<ParticipantDBEntity> participantDBEntities = entity.getParticipants();
        insertParticipants(participantDBEntities);
        List<TaskParticipantJoin> taskParticipantJoins = new ArrayList<>();
        List<BoardParticipantJoin> boardParticipantJoins = new ArrayList<>();
        int taskId = taskDBEntity.getTaskId();
        int boardId = taskDBEntity.getBoardId();
        for (ParticipantDBEntity participantDBEntity : participantDBEntities) {
            TaskParticipantJoin taskParticipantJoin = new TaskParticipantJoin();
            BoardParticipantJoin boardParticipantJoin = new BoardParticipantJoin();
            taskParticipantJoin.setTaskId(taskId);
            taskParticipantJoin.setAddress(participantDBEntity.getAddress());
            boardParticipantJoin.setBoardId(boardId);
            boardParticipantJoin.setAddress(participantDBEntity.getAddress());
        }
        Calendar calendar = Calendar.getInstance();
        LastUpdateEntity lastUpdateEntity = new LastUpdateEntity();
        lastUpdateEntity.setUpdated(calendar);
        lastUpdateEntity.setId(taskId);
        lastUpdateEntity.setQueryType(TASK);
        updateLastUpdate(lastUpdateEntity);
    }

    @Transaction
    public void addTaskParticipant(int taskId, ParticipantDBEntity participantDBEntity) {
        insertParticipant(participantDBEntity);
        TaskParticipantJoin taskParticipantJoin = new TaskParticipantJoin();
        taskParticipantJoin.setTaskId(taskId);
        taskParticipantJoin.setAddress(participantDBEntity.getAddress());
        insertTaskParticipantJoin(taskParticipantJoin);
        BoardParticipantJoin boardParticipantJoin = new BoardParticipantJoin();
        int boardId = getBoardId(taskId);
        boardParticipantJoin.setBoardId(boardId);
        boardParticipantJoin.setAddress(participantDBEntity.getAddress());
        insertBoardParticipantJoin(boardParticipantJoin);
    }

    @Query("SELECT groupId, title as groupTitle FROM groups WHERE boardId = :boardId")
    public abstract LiveData<List<GroupShortInfo>> getGroupsShortInfo(int boardId);

    @Query("DELETE FROM task_participant_join WHERE taskId = :taskId AND address = :address")
    public abstract void removeTaskParticipant(int taskId, String address);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTaskDBEntity(TaskDBEntity taskDBEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertParticipants(List<ParticipantDBEntity> participantDBEntities);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertParticipant(ParticipantDBEntity participantDBEntity);

    @Insert
    public abstract void insertTaskParticipantJoins(List<TaskParticipantJoin> taskParticipantJoins);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertTaskParticipantJoin(TaskParticipantJoin taskParticipantJoin);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertBoardParticipantJoins(List<BoardParticipantJoin> boardParticipantJoins);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertBoardParticipantJoin(BoardParticipantJoin boardParticipantJoin);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertTaskTitle(TaskDBEntity taskDBEntity);

    @Query("DELETE FROM board_participant_join " +
            "WHERE boardId = :boardId and address = :address")
    public abstract void removeBoardEntityParticipant(int boardId, String address);

//    @Transaction
//    public void insertTask(Task task) {
//        TaskDBEntity taskDBEntity = new TaskDBEntity(task);
//        List<TaskParticipantJoin> taskParticipantJoins = new ArrayList<>();
//        for (ParticipantDBEntity e: task.getParticipants()) {
//            TaskParticipantJoin taskParticipantJoin = new TaskParticipantJoin();
//            taskParticipantJoin.setTaskId(task.getTaskId());
//            taskParticipantJoin.setAddress(e.getAddress());
//            taskParticipantJoins.add(taskParticipantJoin);
//        }
//        insertTaskDBEntity(taskDBEntity);
//        insertParticipants(task.getParticipants());
//        insertTaskParticipantJoins(taskParticipantJoins);
//    }

    @Query("DELETE FROM task_participant_join " +
            "WHERE taskId in (SELECT taskId FROM tasks WHERE boardId = :boardId) AND address = :address")
    public abstract void removeTaskParticipantJoinsOfBoard(int boardId, String address);

    public void removeBoardParticipant(int boardId, String address) {
        removeBoardEntityParticipant(boardId, address);
        removeTaskParticipantJoinsOfBoard(boardId, address);
    }

    @Query("DELETE FROM boards")
    public abstract void clearBoards();

    @Query("DELETE FROM last_update")
    public abstract void clearUpdates();

    @Query("SELECT count(*) FROM boards")
    public abstract int getBoardsCount();

    @Insert
    public abstract void insertBoards(List<BoardDBEntity> boardDBEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateLastUpdate(LastUpdateEntity lastUpdateEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateLastUpdates(List<LastUpdateEntity> lastUpdateEntities);

    @Transaction
    public void updateBoards(List<BoardDBEntity> boardDBEntities) {
        clearData();
        insertBoards(boardDBEntities);
        LastUpdateEntity lastUpdateEntity = new LastUpdateEntity();
        lastUpdateEntity.setQueryType(LastUpdateEntity.Query.BOARDS);
        lastUpdateEntity.setId(-1);
        lastUpdateEntity .setUpdated(Calendar.getInstance());
        updateLastUpdate(lastUpdateEntity);
    }

    public void clearData() {
        clearBoards();
        clearUpdates();
    }

    @Query("SELECT groups.title, count(tasks.taskId) as tasksCount " +
            "FROM groups " +
            "INNER JOIN tasks ON tasks.groupId = groups.groupId " +
            "WHERE groups.boardId = :boardId " +
            "GROUP BY groups.groupId")
    public abstract LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId);

    @Query("SELECT title FROM boards WHERE boardId = :id")
    public abstract LiveData<String> getBoardTitle(int id);

    @Query("SELECT tasks.taskId, tasks.title, tasks.groupId " +
            "FROM tasks INNER JOIN task_participant_join as tpj ON tasks.taskId = tpj.taskId " +
            "WHERE tpj.address = :address AND tasks.boardId = :boardId")
    public abstract LiveData<List<TaskCard>> getUserTaskCards(int boardId, String address);

    @Transaction
    public void updateBoard(Board board) {
        int boardId = board.getBoardDBEntity().getBoardId();
        removeBoard(boardId);
        insertBoard(board.getBoardDBEntity());
        insertParticipants(board.getParticipants());
        List<BoardParticipantJoin> boardParticipantJoins = new ArrayList<>();
        for (ParticipantDBEntity participantDBEntity : board.getParticipants()) {
            BoardParticipantJoin boardParticipantJoin = new BoardParticipantJoin();
            boardParticipantJoin.setAddress(participantDBEntity.getAddress());
            boardParticipantJoin.setBoardId(boardId);
            boardParticipantJoins.add(boardParticipantJoin);
        }
        for (BoardGroup boardGroup : board.getBoardGroups()) {
            updateBoardGroup(boardGroup.getGroupDBEntity(), boardGroup.getTasks());
        }
        Calendar calendar = Calendar.getInstance();
        insertBoardParticipantJoins(boardParticipantJoins);
        LastUpdateEntity lastUpdateEntity = new LastUpdateEntity();
        lastUpdateEntity.setQueryType(LastUpdateEntity.Query.BOARD);
        lastUpdateEntity.setId(boardId);
        lastUpdateEntity.setUpdated(calendar);
        List<LastUpdateEntity> taskUpdates = new ArrayList<>();
        for (BoardGroup boardGroup : board.getBoardGroups()) {
            for (TaskDBEntityWithParticipantsAddresses task : boardGroup.getTasks()) {
                LastUpdateEntity taskUpdate = new LastUpdateEntity();
                taskUpdate.setQueryType(TASK);
                taskUpdate.setUpdated(calendar);
                taskUpdate.setId(task.getTaskDBEntity().getTaskId());
                taskUpdates.add(taskUpdate);
            }
        }
        updateLastUpdate(lastUpdateEntity);
        updateLastUpdates(taskUpdates);
    }

    @Query("SELECT p.* " +
        "FROM participants as p " +
        "INNER JOIN board_participant_join as bpj ON bpj.address = p.address " +
        "WHERE bpj.boardId = :boardId")
    public abstract LiveData<List<ParticipantDBEntity>> getBoardParticipants(int boardId);

    @Query("DELETE FROM boards where boardId = :id")
    public abstract void removeBoard(int id);

    @Query("SELECT updated FROM last_update WHERE queryType = :queryType and id = :id")
    public abstract LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id);

    @Query("SELECT boards.*, last_update.updated FROM last_update LEFT JOIN boards WHERE last_update.queryType = 'BOARDS' and last_update.id = -1")
    public abstract LiveData<List<BoardWithUpdate>> getBoardCards();

    @Query("SELECT groups.*, last_update.updated FROM last_update LEFT JOIN groups ON groups.boardId = last_update.id WHERE last_update.queryType = 'BOARD' and last_update.id = :boardId")
    public abstract LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId);

    @Query("SELECT taskId, title, groupId FROM tasks  WHERE groupId = :groupId")
    public abstract LiveData<List<TaskCard>> getTaskCards(int groupId);

    @Query("SELECT start, finish FROM boards WHERE boardId = :boardId")
    public abstract LiveData<StartFinishDates> getBoardStartFinishDates(int boardId);

    @Query("SELECT title FROM groups WHERE groupId = :groupId")
    public abstract LiveData<String> getGroupTitle(int groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTaskDBEntities(List<TaskDBEntity> taskDBEntities);

    public void updateBoardGroup(GroupDBEntity groupDBEntity, List<TaskDBEntityWithParticipantsAddresses> tasks) {
        insertGroup(groupDBEntity);
        List<TaskDBEntity> taskDBEntities = new ArrayList<>();
        List<TaskParticipantJoin> tasksParticipants = new ArrayList<>();
        for (TaskDBEntityWithParticipantsAddresses t : tasks) {
            taskDBEntities.add(t.getTaskDBEntity());
            for (String participant : t.getParticipants()) {
                TaskParticipantJoin taskParticipantJoin = new TaskParticipantJoin();
                taskParticipantJoin.setTaskId(t.getTaskDBEntity().getTaskId());
                taskParticipantJoin.setAddress(participant);
                tasksParticipants.add(taskParticipantJoin);
            }
        }
        insertTaskDBEntities(taskDBEntities);
        insertTaskParticipantJoins(tasksParticipants);
        Calendar calendar = Calendar.getInstance();
        List<LastUpdateEntity> tasksUpdated = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            LastUpdateEntity taskUpdateEntity = new LastUpdateEntity();
            taskUpdateEntity.setQueryType(TASK);
            taskUpdateEntity.setId(tasks.get(i).getTaskDBEntity().getTaskId());
            taskUpdateEntity.setUpdated(calendar);
            tasksUpdated.add(taskUpdateEntity);
        }
        updateLastUpdates(tasksUpdated);
    }

    @Query("SELECT p.* " +
            "FROM participants as p " +
            "INNER JOIN task_participant_join as tpj ON tpj.address = p.address " +
            "WHERE tpj.taskId = :taskId")
    public abstract LiveData<List<ParticipantDBEntity>> getTaskParticipants(int taskId);

    @Query("UPDATE tasks SET groupId = :groupId WHERE taskId = :taskId")
    public abstract void setTaskGroup(int taskId, int groupId);

    @Query("UPDATE tasks SET description = :description WHERE taskId = :taskId")
    public abstract void setTaskDescription(int taskId, String description);

    @Query("UPDATE tasks SET title = :title WHERE taskId = :taskId")
    public abstract void setTaskTitle(int taskId, String title);

    @Query("UPDATE tasks SET start = :calendar WHERE taskId = :taskId")
    public abstract void setTaskStart(int taskId, Calendar calendar);

    @Query("UPDATE tasks SET finish = :calendar WHERE taskId = :taskId")
    public abstract void setTaskFinish(int taskId, Calendar calendar);

    @Query("UPDATE boards SET start = :calendar WHERE boardId = :boardId")
    public abstract void setBoardStart(int boardId, Calendar calendar);

    @Query("UPDATE boards SET finish = :calendar WHERE boardId = :boardId")
    public abstract void setBoardFinish(int boardId, Calendar calendar);

    @Query("SELECT title, start, finish FROM tasks WHERE boardId = :boardId and start IS NOT NULL and finish IS NOT NULL and start <= finish ORDER BY finish DESC")// and finish != NULL and start != NULL and start <= finish")
    public abstract LiveData<List<TaskCalendarCard>> getTasksCalendarCard(int boardId);
}
