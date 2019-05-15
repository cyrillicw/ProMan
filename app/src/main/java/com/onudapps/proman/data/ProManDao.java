package com.onudapps.proman.data;

import androidx.room.*;
import com.onudapps.proman.data.db.entities.*;
import com.onudapps.proman.data.pojo.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Dao
public abstract class ProManDao {
    @Insert
    public abstract void insertBoard(BoardDBEntity boardDBEntity);

    @Insert
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

    @Transaction
    public Task getTask(UUID id) {
        Task task = getTaskWithNoParticipants(id);
        List<ParticipantDBEntity> participants = getTaskParticipants(id);
        task.setParticipants(participants);
        return task;
    }

    @Query("DELETE FROM task_participant_join as tpj WHERE tpj.taskId = :id")
    public abstract void deleteTaskParticipants(UUID id);

    @Insert
    public abstract void insertTaskDBEntity(TaskDBEntity taskDBEntity);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertParticipants(List<ParticipantDBEntity> participantDBEntities);

    @Insert
    public abstract void insertTaskParticipantJoins(List<TaskParticipantJoin> taskParticipantJoins);


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
}
