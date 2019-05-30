package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.contracts.Smart;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private Smart smartContract;
    public RemoteDataSource() {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.103:7545"));
        Credentials credentials = Credentials.create("1dae1d2536cfe598574a4b2ba677b5a7173b4ae31052d2ce9e727a98b1709632");
        smartContract = Smart.load("D353369D755253F56E868feDe85c1B99A521F844", web3j, credentials, new DefaultGasProvider());
    }

//    public Flowable<TransactionReceipt> addBoard(String title) {
//        try {
//            //TransactionReceipt transactionReceipt = smartContract.addBoard("gg", BigInteger.valueOf(0), BigInteger.valueOf(1)).send();
//            return smartContract.addBoard(title).flowable();
////            Flowable<Smart.BoardAddedEventResponse> boardAddedEventResponseFlowable = smartContract.boardAddedEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
////            boardAddedEventResponseFlowable.subscribe(event -> {
////                Log.e(LOG_TAG, event.id.toString());
////            });
////            Log.e(LOG_TAG, "tx");
//        }
//        catch (Exception e){
//            Log.e(LOG_TAG, "ERROR" + e.getMessage());
//            return null;
//        }
//    }

    public TransactionReceipt createBoard(String title) {
//        smartContract.addBoard(title).sendAsync().thenAccept(tx -> {
//            Log.e(LOG_TAG, "ok");
//        });
        try {
            return smartContract.addBoard(title).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt createGroup(String title, int boardId) {
//        smartContract.addBoard(title).sendAsync().thenAccept(tx -> {
//            Log.e(LOG_TAG, "ok");
//        });
        try {
            return smartContract.addGroup(BigInteger.valueOf(boardId), title).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt setTaskStart(int taskId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            return smartContract.setTaskStart(BigInteger.valueOf(taskId), BigInteger.valueOf(time)).send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "start set failed " + taskId + " " + e.getMessage());
            return null;
        }
    }

    public TransactionReceipt setTaskFinish(int taskId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            return smartContract.setTaskFinish(BigInteger.valueOf(taskId), BigInteger.valueOf(time)).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt createTask(int groupId, String title) {
        try {
            return smartContract.addTask(BigInteger.valueOf(groupId), title).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public Tuple2<BoardDBEntity, List<Tuple2<GroupDBEntity, List<TaskDBEntity>>>> getBoard(int boardId) {
        try {
            Tuple4<String, BigInteger, BigInteger, List<BigInteger>> tuple
                    = smartContract.getBoard(BigInteger.valueOf(boardId)).send();
            BoardDBEntity boardDBEntity = new BoardDBEntity();
            boardDBEntity.setBoardId(boardId);
            boardDBEntity.setTitle(tuple.getValue1());
            boardDBEntity.setStart(Repository.REPOSITORY.numToCalendar(tuple.getValue2()));
            boardDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(tuple.getValue3()));
            List<Tuple2<GroupDBEntity, List<TaskDBEntity>>> groupsWithTasks = new ArrayList<>();
            for (BigInteger groupId : tuple.getValue4()) {
                Tuple2<String, List<BigInteger>> groupTuple = smartContract.getGroup(groupId).send();
                GroupDBEntity groupDBEntity = new GroupDBEntity();
                groupDBEntity.setGroupId(groupId.intValue());
                groupDBEntity.setBoardId(boardId);
                groupDBEntity.setTitle(groupTuple.getValue1());
                List<TaskDBEntity> taskDBEntities = new ArrayList<>();
                for (BigInteger taskId : groupTuple.getValue2()) {
                    TaskDBEntity taskDBEntity = new TaskDBEntity();
                    Tuple4<String, String, BigInteger, BigInteger> taskTuple = smartContract.getTask(taskId).send();
                    taskDBEntity.setTaskId(taskId.intValue());
                    taskDBEntity.setTitle(taskTuple.getValue1());
                    taskDBEntity.setDescription(taskTuple.getValue2());
                    taskDBEntity.setStart(Repository.REPOSITORY.numToCalendar(taskTuple.getValue3()));
                    taskDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(taskTuple.getValue4()));
                    taskDBEntity.setGroupId(groupId.intValue());
                    taskDBEntity.setBoardId(boardId);
                    taskDBEntities.add(taskDBEntity);
                }
                groupsWithTasks.add(new Tuple2<>(groupDBEntity, taskDBEntities));
            }
            return new Tuple2<>(boardDBEntity, groupsWithTasks);
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "FAILED LOADING BOARD");
            return null;
        }
    }

    public Smart getSmartContract() {
        return smartContract;
    }

    //Tuple3<String, List<BigInteger>, List<String>> res =



    public List<BoardDBEntity> getBoards() {
        try {
            List<BigInteger> boards = smartContract.getBoardsIndices().send();
            List<BoardDBEntity> boardDBEntities = new ArrayList<>();
            Log.e(LOG_TAG, "boards size " + boards.size());
            for (BigInteger id: boards) {
                Tuple3<String, BigInteger, BigInteger> tuple =
                        smartContract.getBoardCard(id).send();
                BoardDBEntity boardDBEntity = new BoardDBEntity();
                boardDBEntity.setBoardId(id.intValue());
                boardDBEntity.setTitle(tuple.getValue1());
                Calendar start = Repository.REPOSITORY.numToCalendar(tuple.getValue2());
                Calendar finish = Repository.REPOSITORY.numToCalendar(tuple.getValue3());
                boardDBEntity.setStart(start);
                boardDBEntity.setFinish(finish);
                boardDBEntities.add(boardDBEntity);
            }
            return boardDBEntities;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed loading boards " + e.getMessage());
            return null;
        }
    }

//    public Tuple4<String, BigInteger, BigInteger, List<BigInteger>> getBoard(int id) {
//        try {
//            return smartContract.getBoard(BigInteger.valueOf(id)).send();
//        }
//        catch (Exception e){
//            Log.e(LOG_TAG, "Failed loading board " + e.getMessage());
//            return null;
//        }
//    }

    public RemoteCall<TransactionReceipt> leaveBoard(int id) {
        return smartContract.leaveBoard(BigInteger.valueOf(id));
    }

//    public Task getTask(UUID id) {
//        try {
//            return smartContract.getTask(id.toString()).send();
//        }
//        catch (Exception e) {
//            Log.e(LOG_TAG, e.getMessage());
//            return null;
//        }
//    }
}
