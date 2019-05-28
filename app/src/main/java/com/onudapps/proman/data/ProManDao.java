package com.onudapps.proman.data;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.onudapps.proman.data.db.entities.*;
import com.onudapps.proman.data.pojo.*;
import org.web3j.tuples.generated.Tuple2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Dao
public abstract class ProManDao {
    private static final String LOG_TAG = "ProManDao";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBoard(BoardDBEntity boardDBEntity);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGroup(GroupDBEntity groupDBEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(ParticipantDBEntity participantDBEntity);

    @Query("SELECT tasks.taskId as taskId, tasks.title as title, tasks.description as description, tasks.start as start, tasks.finish as finish, boards.title as boardTitle, groups.title as groupTitle, tasks.boardId as boardId, tasks.groupId as groupId " +
            "FROM tasks " +
            "INNER JOIN boards ON tasks.boardId = boards.boardId " +
            "INNER JOIN groups ON tasks.groupId = groups.groupId " +
            "WHERE tasks.taskId = :id")
    public abstract Task getTaskWithNoParticipants(UUID id);

    @Query("SELECT p.publicKey, p.nickName " +
            "FROM participants as p " +
            "INNER JOIN task_participant_join as tpj ON tpj.participantPublicKey = p.publicKey " +
            "WHERE tpj.taskId = :id")
    public abstract List<ParticipantDBEntity> getTaskParticipants(UUID id);

    @Query("SELECT * FROM tasks WHERE tasks.taskId = :taskId")
    public abstract LiveData<TaskDBEntity> getTaskDBEntity(int taskId);

    @Transaction
    public Task getTask(UUID id) {
        Task task = getTaskWithNoParticipants(id);
        List<ParticipantDBEntity> participants = getTaskParticipants(id);
        task.setParticipants(participants);
        return task;
    }

    @Query("DELETE FROM task_participant_join as tpj WHERE tpj.taskId = :id")
    public abstract void deleteTaskParticipants(UUID id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTaskDBEntity(TaskDBEntity taskDBEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertParticipants(List<ParticipantDBEntity> participantDBEntities);

    @Insert
    public abstract void insertTaskParticipantJoins(List<TaskParticipantJoin> taskParticipantJoins);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertTaskTitle(TaskDBEntity taskDBEntity);


    @Transaction
    public void insertTask(Task task) {
        TaskDBEntity taskDBEntity = new TaskDBEntity(task);
        List<TaskParticipantJoin> taskParticipantJoins = new ArrayList<>();
        for (ParticipantDBEntity e: task.getParticipants()) {
            TaskParticipantJoin taskParticipantJoin = new TaskParticipantJoin();
            taskParticipantJoin.setTaskId(task.getTaskId());
            taskParticipantJoin.setParticipantPublicKey(e.getPublicKey());
            taskParticipantJoins.add(taskParticipantJoin);
        }
        insertTaskDBEntity(taskDBEntity);
        insertParticipants(task.getParticipants());
        insertTaskParticipantJoins(taskParticipantJoins);
    }

    @Query("DELETE FROM boards")
    public abstract void clearBoards();

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
        clearBoards();
        Log.e(LOG_TAG, "size " + getBoardsCount());
        insertBoards(boardDBEntities);
        LastUpdateEntity lastUpdateEntity = new LastUpdateEntity();
        lastUpdateEntity.setQueryType(LastUpdateEntity.Query.BOARDS);
        lastUpdateEntity.setId(-1);
        lastUpdateEntity .setUpdated(Calendar.getInstance());
        updateLastUpdate(lastUpdateEntity);
    }

    @Query("SELECT groups.title, count(tasks.taskId) as tasksCount " +
            "FROM groups " +
            "INNER JOIN tasks ON tasks.groupId = groups.groupId " +
            "WHERE groups.boardId = :boardId " +
            "GROUP BY groups.groupId")
    public abstract LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId);

    @Query("SELECT title FROM boards WHERE boardId = :id")
    public abstract LiveData<String> getBoardTitle(int id);

    @Transaction
    public void updateBoard(BoardDBEntity boardDBEntity, List<Tuple2<GroupDBEntity, List<TaskDBEntity>>> groups) {
        insertBoard(boardDBEntity);
        for (Tuple2<GroupDBEntity, List<TaskDBEntity>> tuple : groups) {
            updateBoardGroup(tuple.getValue1(), tuple.getValue2());
        }
        LastUpdateEntity lastUpdateEntity = new LastUpdateEntity();
        lastUpdateEntity.setQueryType(LastUpdateEntity.Query.BOARD);
        lastUpdateEntity.setId(boardDBEntity.getBoardId());
        lastUpdateEntity.setUpdated(Calendar.getInstance());
        updateLastUpdate(lastUpdateEntity);
    }

    @Query("DELETE FROM boards where boardId = :id")
    public abstract void removeBoard(int id);

    @Query("SELECT updated FROM last_update WHERE queryType = :queryType and id = :id")
    public abstract LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id);

    @Query("SELECT boards.*, last_update.updated FROM last_update LEFT JOIN boards WHERE last_update.queryType = 'BOARDS' and last_update.id = -1")
    public abstract LiveData<List<BoardWithUpdate>> getBoardCards();

    @Query("SELECT groups.*, last_update.updated FROM last_update LEFT JOIN groups ON groups.boardId = last_update.id WHERE last_update.queryType = 'BOARD' and last_update.id = :boardId")
    public abstract LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId);

    @Query("SELECT taskId, title FROM tasks WHERE groupId = :groupId")
    public abstract LiveData<List<TaskCard>> getTaskCards(int groupId);

    @Query("SELECT title FROM groups WHERE groupId = :groupId")
    public abstract LiveData<String> getGroupTitle(int groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertTaskDBEntities(List<TaskDBEntity> taskDBEntities);

    @Transaction
    public void updateBoardGroup(GroupDBEntity groupDBEntity, List<TaskDBEntity> taskDBEntities) {
        insertGroup(groupDBEntity);
        insertTaskDBEntities(taskDBEntities);
        Calendar calendar = Calendar.getInstance();
        List<LastUpdateEntity> tasksUpdated = new ArrayList<>();
        for (int i = 0; i < taskDBEntities.size(); i++) {
            LastUpdateEntity taskUpdateEntity = new LastUpdateEntity();
            taskUpdateEntity.setQueryType(LastUpdateEntity.Query.TASK);
            taskUpdateEntity.setId(taskDBEntities.get(i).getTaskId());
            taskUpdateEntity.setUpdated(calendar);
            tasksUpdated.add(taskUpdateEntity);
        }
        updateLastUpdates(tasksUpdated);
    }

    @Query("UPDATE tasks SET start = :calendar WHERE taskId = :taskId")
    public abstract void setTaskStart(int taskId, Calendar calendar);

    @Query("UPDATE tasks SET finish = :calendar WHERE taskId = :taskId")
    public abstract void setTaskFinish(int taskId, Calendar calendar);

    @Query("SELECT title, start, finish FROM tasks WHERE boardId = :boardId")// and finish != NULL and start != NULL and start <= finish")
    public abstract LiveData<List<TaskCalendarCard>> getTasksCalendarCard(int boardId);
}
