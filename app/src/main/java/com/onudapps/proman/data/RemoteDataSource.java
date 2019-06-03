package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.BuildConfig;
import com.onudapps.proman.contracts.Smart;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.BoardGroup;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private Smart smartContract;
    public RemoteDataSource(Credentials credentials) {
        Web3j web3j = Web3j.build(new HttpService(BuildConfig.hostAPI));
        smartContract = Smart.load(BuildConfig.contractAddress, web3j, credentials, new DefaultGasProvider());
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

    public TransactionReceipt removeBoardParticipant(int boardId, String address) {
        try {
            return smartContract.removeBoardParticipant(BigInteger.valueOf(boardId), address).send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Removing board's participant failed");
            return null;
        }
    }

    public TransactionReceipt createBoard(String title) {
        try {
            return smartContract.addBoard(title).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt setTaskGroup(int taskId, int groupId) {
        try {
            return smartContract.setTaskGroup(BigInteger.valueOf(taskId), BigInteger.valueOf(groupId)).send();
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

    public TransactionReceipt setTaskDescription(int taskId, String description) {
        try {
            return smartContract.setTaskDescription(BigInteger.valueOf(taskId), description).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt setTaskTitle(int taskId, String title) {
        try {
            return smartContract.setTaskTitle(BigInteger.valueOf(taskId), title).send();
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

    public static boolean  signIn(String privateKey) {
        try {
            Web3j web3j = Web3j.build(new HttpService(BuildConfig.hostAPI));
            Credentials credentials = Credentials.create(privateKey);
            Smart smart = Smart.load(BuildConfig.contractAddress, web3j, credentials, new DefaultGasProvider());
            return smart.signIn().send();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static TransactionReceipt  signUp(String privateKey, String nickname) {
        try {
            Web3j web3j = Web3j.build(new HttpService(BuildConfig.hostAPI));
            Credentials credentials = Credentials.create(privateKey);
            Smart smart = Smart.load(BuildConfig.contractAddress, web3j, credentials, new DefaultGasProvider());
            return smart.signUp(nickname).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    public TransactionReceipt setBoardStart(int boardId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            return smartContract.setBoardStart(BigInteger.valueOf(boardId), BigInteger.valueOf(time)).send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "start set failed " + boardId + " " + e.getMessage());
            return null;
        }
    }

    public TransactionReceipt setBoardFinish(int boardId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            return smartContract.setBoardFinish(BigInteger.valueOf(boardId), BigInteger.valueOf(time)).send();
        }
        catch (Exception e) {
            //Log.e(LOG_TAG, "start set failed " + boardId + " " + e.getMessage());
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

    public Board getBoard(int boardId) {
        try {
            Tuple5<String, BigInteger, BigInteger, List<BigInteger>, List<String>> tuple
                    = smartContract.getBoard(BigInteger.valueOf(boardId)).send();
            Board board = new Board();
            BoardDBEntity boardDBEntity = new BoardDBEntity();
            boardDBEntity.setBoardId(boardId);
            boardDBEntity.setTitle(tuple.getValue1());
            boardDBEntity.setStart(Repository.REPOSITORY.numToCalendar(tuple.getValue2()));
            boardDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(tuple.getValue3()));
            board.setBoardDBEntity(boardDBEntity);
            List<BoardGroup> groupsWithTasks = new ArrayList<>();
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
                BoardGroup boardGroup = new BoardGroup();
                boardGroup.setGroupDBEntity(groupDBEntity);
                boardGroup.setTasks(taskDBEntities);
                groupsWithTasks.add(boardGroup);
            }
            board.setBoardGroups(groupsWithTasks);
            List<ParticipantDBEntity> participantDBEntities = new ArrayList<>();
            for (String s: tuple.getValue5()) {
                ParticipantDBEntity participantDBEntity = new ParticipantDBEntity();
                String nick = smartContract.getUserNick(s).send();
                participantDBEntity.setAddress(s);
                participantDBEntity.setNickName(nick);
                participantDBEntities.add(participantDBEntity);
            }
            board.setParticipants(participantDBEntities);
            return board;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "FAILED LOADING BOARD");
            return null;
        }
    }

    public String addBoardParticipant(int boardId, String address) {
        try {
            TransactionReceipt tx = smartContract.addBoardParticipant(BigInteger.valueOf(boardId), address).send();
            List<Smart.BoardParticipantAddedEventResponse> nicks = smartContract.getBoardParticipantAddedEvents(tx);
            if (nicks.size() > 0 && !nicks.get(0).nick.equals("")) {
                return nicks.get(0).nick;
            }
            return null;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "error " + e.getMessage());
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
