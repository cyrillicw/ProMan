package com.onudapps.proman.data;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import com.onudapps.proman.data.db.entities.*;
import com.onudapps.proman.data.pojo.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.onudapps.proman.ui.activities.StartActivity.PREFERENCES;
import static com.onudapps.proman.ui.activities.StartActivity.PRIVATE_KEY;

public enum  Repository {
    REPOSITORY;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private String address;
    private ExecutorService executorService;
    private boolean active;

    Repository() {
        active = false;
    }

    public static void initialize(Context context) {
        REPOSITORY.localDataSource = new LocalDataSource(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String privateKey = sharedPreferences.getString(PRIVATE_KEY, null);
        Credentials credentials = Credentials.create(privateKey);
        REPOSITORY.address = credentials.getAddress().toLowerCase();
        REPOSITORY.remoteDataSource = new RemoteDataSource(credentials);
        REPOSITORY.executorService = Executors.newSingleThreadExecutor();
        REPOSITORY.active = true;
    }

    public LiveData<List<ParticipantDBEntity>> getBoardParticipants(int boardId) {
        return localDataSource.getBoardParticipants(boardId);
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }

    public void moveTaskToGroup(int taskId, int groupId) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setTaskGroup(taskId, groupId);
                if (tx != null && active) {
                    localDataSource.setTaskGroup(taskId, groupId);
                }
            });
        }
    }

    public void removeBoardParticipant(int boardId, String address) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.removeBoardParticipant(boardId, address);
                if (tx != null && active) {
                    localDataSource.removeBoardParticipant(boardId, address);
                }
            });
        }
    }

    public void removeTaskParticipant(int taskId, String address) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.removeTaskParticipant(taskId, address);
                if (tx != null && active) {
                    localDataSource.removeTaskParticipant(taskId, address);
                }
            });
        }
    }

    public LiveData<List<TaskCard>> getCurrentUserTaskCards(int boardId) {
        return localDataSource.getUserTaskCards(boardId, address);
    }

    public void updateTaskDescription(int taskId, String description) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setTaskDescription(taskId, description);
                if (tx != null && active) {
                    localDataSource.setTaskDescription(taskId, description);
                }
            });
        }
    }

    public void updateTask(int taskId) {
        if (active) {
            executorService.execute(() -> {
                TaskDBEntityWithParticipants taskDBEntityWithParticipants =
                        remoteDataSource.getTask(taskId);
                if (taskDBEntityWithParticipants != null && active) {
                    localDataSource.insertTaskWithParticipants(taskDBEntityWithParticipants);
                }
            });
        }
    }

    public void onSignOut() {
        active = false;
        executorService.execute(() -> {
            localDataSource.onSignOut();
        });
        executorService.shutdown();
    }

    public void updateTaskTitle(int taskId, String title) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setTaskTitle(taskId, title);
                if (tx != null && active) {
                    localDataSource.setTaskTitle(taskId, title);
                }
            });
        }
    }

    public LiveData<List<ParticipantDBEntity>> getTaskParticipants(int taskId) {
        return localDataSource.getTaskParticipants(taskId);
    }

    public LiveData<List<BoardWithUpdate>> getBoardCards() {
        return localDataSource.getBoardCards();
    }

    public LiveData<List<TaskCalendarCard>> getTasksCalendarData(int boardId) {
        return localDataSource.getTasksCalendarData(boardId);
    }

    public LiveData<StartFinishDates> getBoardStartFinishDates(int boardId) {
        return localDataSource.getBoardStartFinishDates(boardId);
    }

    public LiveData<List<GroupWithUpdate>> getBoardGroups(int boardId) {
        return localDataSource.getBoardGroups(boardId);
    }

    public LiveData<Task> getTask(int taskId) {
        return localDataSource.getTask(taskId);
    }

    public LiveData<List<GroupShortInfo>> getGroupsShortInfo(int boardId) {
        return localDataSource.getGroupsShortInfo(boardId);
    }

    public void createBoard(String title) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.createBoard(title);
                if (tx != null && active) {
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
    }

    public void createGroup(String title, int boardId) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.createGroup(title, boardId);
                if (tx != null && active) {
                    Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
                    GroupDBEntity groupDBEntity = new GroupDBEntity();
                    groupDBEntity.setGroupId(id);
                    groupDBEntity.setBoardId(boardId);
                    groupDBEntity.setTitle(title);
                    localDataSource.insertGroup(groupDBEntity);
                }
            });
        }
    }

    public LiveData<String> getBoardTitle(int id) {
        return localDataSource.getBoardTitle(id);
    }

    public void leaveBoard(int id) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt transactionReceipt = remoteDataSource.leaveBoard(id);
                if (transactionReceipt != null && active) {
                    localDataSource.removeBoard(id);
                }
            });
        }
    }

    public void addBoardParticipant(final int boardId, final String address) {
        if (active) {
            executorService.execute(() -> {
                String nick = remoteDataSource.addBoardParticipant(boardId, address);
                if (nick != null && active) {
                    ParticipantDBEntity participantDBEntity = new ParticipantDBEntity();
                    participantDBEntity.setAddress(address);
                    participantDBEntity.setNickName(nick);
                    localDataSource.addBoardParticipant(boardId, participantDBEntity);
                }
            });
        }
    }

    public void addTaskParticipant(final int taskId, final String address) {
        if (active) {
            executorService.execute(() -> {
                String nick = remoteDataSource.addTaskParticipant(taskId, address);
                if (nick != null && active) {
                    ParticipantDBEntity participantDBEntity = new ParticipantDBEntity();
                    participantDBEntity.setAddress(address);
                    participantDBEntity.setNickName(nick);
                    localDataSource.addTaskParticipant(taskId, participantDBEntity);
                }
            });
        }
    }

    public LiveData<List<TaskCard>> getTaskCards(int groupId) {
        return localDataSource.getTaskCards(groupId);
    }

    public LiveData<String> getGroupTitle(int groupId) {
        return localDataSource.getGroupTitle(groupId);
    }

    public void updateBoardCards() {
        if (active) {
            executorService.execute(() -> {
                List<BoardDBEntity> boardDBEntities = remoteDataSource.getBoards();
                if (boardDBEntities != null && active) {
                    localDataSource.updateBoardCards(boardDBEntities);
                }
            });
        }
    }

    public void setTaskStart(int taskId, Calendar calendar) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setTaskStart(taskId, calendar);
                if (tx != null && active) {
                    localDataSource.setTaskStart(taskId, calendar);
                }
            });
        }
    }

    public void setTaskFinish(int taskId, Calendar calendar) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setTaskFinish(taskId, calendar);
                if (tx != null && active) {
                    localDataSource.setTaskFinish(taskId, calendar);
                }
            });
        }
    }

    public void setBoardStart(int boardId, Calendar calendar) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setBoardStart(boardId, calendar);
                if (tx != null && active) {
                    localDataSource.setBoardStart(boardId, calendar);
                }
            });
        }
    }

    public void setBoardFinish(int boardId, Calendar calendar) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.setBoardFinish(boardId, calendar);
                if (tx != null && active) {
                    localDataSource.setBoardFinish(boardId, calendar);
                }
            });
        }
    }

    public void updateBoard(int id) {
        if (active) {
            executorService.execute(() -> {
                Board board
                        = remoteDataSource.getBoard(id);
                if (board != null && active) {
                    localDataSource.updateBoard(board);
                }
            });
        }
    }

    public LiveData<List<GroupStatistic>> getGroupsStatistics(int boardId) {
        return localDataSource.getGroupsStatistics(boardId);
    }

    public void createTask(int boardId, int groupId, String title) {
        if (active) {
            executorService.execute(() -> {
                TransactionReceipt tx = remoteDataSource.createTask(groupId, title);
                if (tx != null && active) {
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
}
