package com.onudapps.proman.data;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.LastUpdateEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum  Repository {
    REPOSITORY;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private ExecutorService executorService;

    public static void initialize(Context context) {
        REPOSITORY.localDataSource = new LocalDataSource(context);
        REPOSITORY.remoteDataSource = new RemoteDataSource();
        REPOSITORY.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<TaskDBEntity> getTaskDBEntity(int taskId) {
        return localDataSource.getTaskDBEntity(taskId);
    }

    public LiveData<List<BoardWithUpdate>> getBoardCards() {
        return localDataSource.getBoardCards();
    }

    public LiveData<List<TaskCalendarCard>> getTasksCalendarData(int boardId) {
        return localDataSource.getTasksCalendarData(boardId);
    }

    public LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId) {
        return localDataSource.getBoardGroups(boardId);
    }

    public void createBoard(String title) {
        executorService.execute(() -> {
            TransactionReceipt tx = remoteDataSource.createBoard(title);
            if (tx != null) {
                Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
                BoardDBEntity boardDBEntity = new BoardDBEntity();
                boardDBEntity.setBoardId(id);
                boardDBEntity.setTitle(title);
                boardDBEntity.setStart(null);
                boardDBEntity.setFinish(null);
                localDataSource.insertBoard(boardDBEntity);
            }
        });
    }

    public void createGroup(String title, int boardId) {
        executorService.execute(() -> {
            TransactionReceipt tx = remoteDataSource.createGroup(title, boardId);
            if (tx != null) {
                Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
                GroupDBEntity groupDBEntity = new GroupDBEntity();
                groupDBEntity.setGroupId(id);
                groupDBEntity.setBoardId(boardId);
                groupDBEntity.setTitle(title);
                localDataSource.insertGroup(groupDBEntity);
            }
        });
    }

    public LiveData<String> getBoardTitle(int id) {
        return localDataSource.getBoardTitle(id);
    }

    public void leaveBoard(int id) {
        executorService.execute(() -> {
            Log.e("IN DELETE", "ID " + id);
            try {
                TransactionReceipt transactionReceipt = remoteDataSource.leaveBoard(id).send();
                localDataSource.removeBoard(id);
                Log.e("REPO" , "answer " + transactionReceipt.getLogs().get(0).getData());
            }
            catch (Exception e) {
                Log.e("REPO", "error" + e.getMessage());
            }
        });
    }

    public LiveData<List<TaskCard>> getTaskCards(int groupId) {
        return localDataSource.getTaskCards(groupId);
    }

    public LiveData<String> getGroupTitle(int groupId) {
        return localDataSource.getGroupTitle(groupId);
    }

    public void updateBoardCards() {
        executorService.execute(() -> {
            List<BoardDBEntity> boardDBEntities = remoteDataSource.getBoards();
            if (boardDBEntities != null) {
                localDataSource.updateBoardCards(boardDBEntities);
            }
        });
    }

    public LiveData<Calendar> getLastUpdate(LastUpdateEntity.Query queryType, int id) {
        return localDataSource.getLastUpdate(queryType, id);
    }

    public void setTaskStart(int taskId, Calendar calendar) {
        executorService.execute(() -> {
            TransactionReceipt tx = remoteDataSource.setTaskStart(taskId, calendar);
            if (tx != null) {
                    localDataSource.setTaskStart(taskId, calendar);
            }
        });
    }

    public void setTaskFinish(int taskId, Calendar calendar) {
        executorService.execute(() -> {
            TransactionReceipt tx = remoteDataSource.setTaskFinish(taskId, calendar);
            if (tx != null) {
                    localDataSource.setTaskFinish(taskId, calendar);
            }
        });
    }

//    private Tuple2<GroupDBEntity, List<TaskDBEntity>> getBoardGroup(int groupId, int boardId) {
//        Tuple6<String, List<BigInteger>, List<String>, List<String>, List<BigInteger>, List<BigInteger>> tuple =
//                remoteDataSource.getGroup(groupId);
//        String title = tuple.getValue1();
//        List<BigInteger> tasksId = tuple.getValue2();
//        List<String> tasksTitle = tuple.getValue3();
//        List<String> tasksDescription = tuple.getValue4();
//        List<BigInteger> tasksStart = tuple.getValue5();
//        List<BigInteger> tasksFinish = tuple.getValue6();
//        List<TaskDBEntity> taskDBEntities = new ArrayList<>();
//        for (int i = 0; i < tasksTitle.size(); i++) {
//            TaskDBEntity taskDBEntity = new TaskDBEntity();
//            taskDBEntity.setTaskId(tasksId.get(i).intValue());
//            taskDBEntity.setTitle(tasksTitle.get(i));
//            taskDBEntity.setDescription(tasksDescription.get(i));
//            Calendar start = numToCalendar(tasksStart.get(i));
//            Calendar finish = numToCalendar(tasksFinish.get(i));
//            taskDBEntity.setStart(start);
//            taskDBEntity.setFinish(finish);
//            taskDBEntity.setBoardId(boardId);
//            taskDBEntity.setGroupId(groupId);
//            taskDBEntities.add(taskDBEntity);
//        }
//        GroupDBEntity groupDBEntity = new GroupDBEntity();
//        groupDBEntity.setTitle(title);
//        groupDBEntity.setBoardId(boardId);
//        groupDBEntity.setGroupId(groupId);
//        return new Tuple2<>(groupDBEntity, taskDBEntities);
////        GroupDBEntity groupDBEntity = new GroupDBEntity();
////        groupDBEntity.setTitle("F");
////        groupDBEntity.setBoardId(boardId);
////        groupDBEntity.setGroupId(groupId);
////        List<TaskDBEntity> taskDBEntities = new ArrayList<>();
////        for (int i = 0; i < 8; i++) {
////            TaskDBEntity taskDBEntity = new TaskDBEntity();
////            taskDBEntity.setTitle("TASKАААААFFFFFFFFFFFFFFFААААА " + i);
////            Calendar start = Calendar.getInstance();
////            start.add(Calendar.DAY_OF_MONTH, -i - 1);
////            Calendar finish = Calendar.getInstance();
////            finish.add(Calendar.DAY_OF_MONTH, i + 1);
////            taskDBEntity.setTaskId((int)(Math.random() * 100000));
////            taskDBEntity.setStart(start);
////            taskDBEntity.setFinish(finish);
////            taskDBEntity.setGroupId(groupId);
////            taskDBEntity.setBoardId(boardId);
////            taskDBEntities.add(taskDBEntity);
////        }
//        return new Tuple2<>(groupDBEntity, taskDBEntities);
//    }

    public void updateBoard(int id) {
        executorService.execute(() -> {
            Tuple2<BoardDBEntity, List<Tuple2<GroupDBEntity, List<TaskDBEntity>>>> board
                    = remoteDataSource.getBoard(id);
            if (board != null) {
                localDataSource.updateBoard(board.getValue1(), board.getValue2());
            }
        });
    }

    public LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId) {
        return localDataSource.getGroupsStatistics(boardId);
    }

    public void createTask(int boardId, int groupId, String title) {
        executorService.execute(() -> {
            TransactionReceipt tx = remoteDataSource.createTask(groupId, title);
            if (tx != null) {
                Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
                TaskDBEntity taskDBEntity = new TaskDBEntity();
                taskDBEntity.setTaskId(id);
                taskDBEntity.setBoardId(boardId);
                taskDBEntity.setGroupId(groupId);
                taskDBEntity.setTitle(title);
                taskDBEntity.setDescription("");
                taskDBEntity.setStart(null);
                taskDBEntity.setFinish(null);
                localDataSource.insertTaskDBEntity(taskDBEntity);
        }
        });
    }

    private BoardDBEntity tupleToBoardDBEntity(int id, Tuple4<String, BigInteger, BigInteger, List<BigInteger>> tuple) {
        BoardDBEntity boardDBEntity = new BoardDBEntity();
        boardDBEntity.setBoardId(id);
        boardDBEntity.setTitle(tuple.getValue1());
        boardDBEntity.setStart(numToCalendar(tuple.getValue2()));
        boardDBEntity.setFinish(numToCalendar(tuple.getValue3()));
        return boardDBEntity;
    }

    public Calendar numToCalendar(BigInteger bigInteger) {
        long date = bigInteger.longValue();
        if (date < 0) {
            return null;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return calendar;
        }
    }

//    public Task getTaskFromServer(UUID id) {
//        Task task = remoteDataSource.getTask(id);
//        if (task != null) {
//            localDataSource.insertTask(task);
//        }
//        return task;
//    }

//    public void updateTask(Task task) {
//        localDataSource.update
//    }
}
