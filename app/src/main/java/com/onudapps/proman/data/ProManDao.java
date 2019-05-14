package com.onudapps.proman.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.entities.Task;

import java.util.List;
import java.util.UUID;

@Dao
public abstract class ProManDao {
    @Insert
    public abstract void insertBoard(BoardDBEntity boardDBEntity);

    @Insert
    public abstract void insertGroup(GroupDBEntity groupDBEntity);

    @Query("SELECT tasks.taskId as taskId, tasks.title as title, tasks.description as description, tasks.start as start, tasks.finish as finish, boards.title as boardTitle, groups.title as groupTitle " +
            "FROM tasks " +
            "INNER JOIN boards ON tasks.boardId = boards.boardId " +
            "INNER JOIN groups ON tasks.groupId = groups.groupId " +
            "WHERE tasks.taskId = :id")
    public abstract Task getTaskWithNoParticipants(UUID id);

    @Query("SELECT p.nickName " +
            "FROM participants as p " +
            "INNER JOIN task_participant_join as tpj ON tpj.participantPublicKey = p.publicKey " +
            "WHERE tpj.taskId = :id")
    public abstract List<String> getTaskParticipants(UUID id);

    @Transaction
    public Task getTask(UUID id) {
        Task task = getTaskWithNoParticipants(id);
        List<String> participants = getTaskParticipants(id);
        task.setParticipants(participants);
        return task;
    }
}
