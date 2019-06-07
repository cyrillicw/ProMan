package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.contracts.ProManSmartContract;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.BoardGroup;
import com.onudapps.proman.data.pojo.TaskDBEntityWithParticipants;
import com.onudapps.proman.data.pojo.TaskDBEntityWithParticipantsAddresses;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private static final String HOST_API = "http://192.168.1.102:7545";
    private static final String CONTRACT_ADDRESS = "fBf07DAe90F50A062a7b76899a5488BEF5b645c6";
    private ProManSmartContract proManSmartContract;
    RemoteDataSource(Credentials credentials) {
        Web3j web3j = Web3j.build(new HttpService(HOST_API));
        proManSmartContract = ProManSmartContract.load(CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
    }

    TransactionReceipt removeBoardParticipant(int boardId, String address) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.removeBoardParticipant(BigInteger.valueOf(boardId), address).send();
            Log.e(LOG_TAG, "Succeeded removing board participant");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed removing board participant");
            return null;
        }
    }

    TransactionReceipt createBoard(String title) {
        try {
            TransactionReceipt tx = proManSmartContract.addBoard(title).send();
            Log.e(LOG_TAG, "Succeeded creating board");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed creating board");
            return null;
        }
    }

    TaskDBEntityWithParticipants getTask(int taskId) {
        try {
            Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, List<String>> tuple
                    = proManSmartContract.getTask(BigInteger.valueOf(taskId)).send();
            TaskDBEntity taskDBEntity = new TaskDBEntity();
            taskDBEntity.setTaskId(taskId);
            taskDBEntity.setTitle(tuple.getValue1());
            taskDBEntity.setDescription(tuple.getValue2());
            taskDBEntity.setStart(Repository.REPOSITORY.numToCalendar(tuple.getValue3()));
            taskDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(tuple.getValue4()));
            taskDBEntity.setGroupId(tuple.getValue5().intValue());
            taskDBEntity.setBoardId(tuple.getValue6().intValue());
            List<ParticipantDBEntity> participantDBEntities = getParticipants(tuple.getValue7());
            TaskDBEntityWithParticipants taskDBEntityWithParticipants = new TaskDBEntityWithParticipants();
            taskDBEntityWithParticipants.setTaskDBEntity(taskDBEntity);
            taskDBEntityWithParticipants.setParticipants(participantDBEntities);
            Log.e(LOG_TAG, "Succeeded loading task");
            return taskDBEntityWithParticipants;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed loading task");
            return null;
        }
    }

    private List<ParticipantDBEntity> getParticipants(List<String> addresses) throws Exception{
        List<ParticipantDBEntity> participantDBEntities = new ArrayList<>();
        for (String s: addresses) {
            ParticipantDBEntity participantDBEntity = new ParticipantDBEntity();
            String nick = proManSmartContract.getUserNick(s).send();
            participantDBEntity.setAddress(s);
            participantDBEntity.setNickName(nick);
            participantDBEntities.add(participantDBEntity);
        }
        return participantDBEntities;
    }

    TransactionReceipt setTaskGroup(int taskId, int groupId) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.setTaskGroup(BigInteger.valueOf(taskId), BigInteger.valueOf(groupId)).send();
            Log.e(LOG_TAG, "Succeeded updating task group");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed updating task group");
            return null;
        }
    }

    TransactionReceipt createGroup(String title, int boardId) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.addGroup(BigInteger.valueOf(boardId), title).send();
            Log.e(LOG_TAG, "Succeeded creating group");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed creating group");
            return null;
        }
    }

    TransactionReceipt setTaskDescription(int taskId, String description) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.setTaskDescription(BigInteger.valueOf(taskId), description).send();
            Log.e(LOG_TAG, "Succeeded setting task description");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting task description");
            return null;
        }
    }

    TransactionReceipt setTaskTitle(int taskId, String title) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.setTaskTitle(BigInteger.valueOf(taskId), title).send();
            Log.e(LOG_TAG, "Succeeded setting task title");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting task title");
            return null;
        }
    }

    TransactionReceipt setTaskStart(int taskId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            TransactionReceipt tx =
                    proManSmartContract.setTaskStart(BigInteger.valueOf(taskId), BigInteger.valueOf(time)).send();
            Log.e(LOG_TAG, "Succeeded setting task start");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting task start");
            return null;
        }
    }

    TransactionReceipt setTaskFinish(int taskId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            TransactionReceipt tx =
                    proManSmartContract.setTaskFinish(BigInteger.valueOf(taskId), BigInteger.valueOf(time)).send();
            Log.e(LOG_TAG, "Succeeded setting task finish");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting task finish");
            return null;
        }
    }

    public static boolean signIn(String privateKey) {
        try {
            Web3j web3j = Web3j.build(new HttpService(HOST_API));
            Credentials credentials = Credentials.create(privateKey);
            ProManSmartContract proManSmartContract = ProManSmartContract.load(CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
            return proManSmartContract.signIn().send();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static TransactionReceipt signUp(String privateKey, String nickname) {
        try {
            Web3j web3j = Web3j.build(new HttpService(HOST_API));
            Credentials credentials = Credentials.create(privateKey);
            ProManSmartContract proManSmartContract = ProManSmartContract.load(CONTRACT_ADDRESS, web3j, credentials, new DefaultGasProvider());
            return proManSmartContract.signUp(nickname).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    TransactionReceipt setBoardStart(int boardId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            TransactionReceipt tx =
                    proManSmartContract.setBoardStart(BigInteger.valueOf(boardId), BigInteger.valueOf(time)).send();
            Log.e(LOG_TAG, "Succeeded setting board start");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting board start");
            return null;
        }
    }

    TransactionReceipt setBoardFinish(int boardId, Calendar calendar) {
        long time = calendar == null ? -1 : calendar.getTimeInMillis();
        try {
            TransactionReceipt tx =
                    proManSmartContract.setBoardFinish(BigInteger.valueOf(boardId), BigInteger.valueOf(time)).send();
            Log.e(LOG_TAG, "Succeeded setting board finish");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed setting board finish");
            return null;
        }
    }

    TransactionReceipt createTask(int groupId, String title) {
        try {
            TransactionReceipt tx =
                    proManSmartContract.addTask(BigInteger.valueOf(groupId), title).send();
            Log.e(LOG_TAG, "Succeeded creating task");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed creating task");
            return null;
        }
    }

    Board getBoard(int boardId) {
        try {
            Tuple5<String, BigInteger, BigInteger, List<BigInteger>, List<String>> tuple
                    = proManSmartContract.getBoard(BigInteger.valueOf(boardId)).send();
            Board board = new Board();
            BoardDBEntity boardDBEntity = new BoardDBEntity();
            boardDBEntity.setBoardId(boardId);
            boardDBEntity.setTitle(tuple.getValue1());
            boardDBEntity.setStart(Repository.REPOSITORY.numToCalendar(tuple.getValue2()));
            boardDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(tuple.getValue3()));
            board.setBoardDBEntity(boardDBEntity);
            List<BoardGroup> groupsWithTasks = new ArrayList<>();
            for (BigInteger groupId : tuple.getValue4()) {
                Tuple2<String, List<BigInteger>> groupTuple = proManSmartContract.getGroup(groupId).send();
                GroupDBEntity groupDBEntity = new GroupDBEntity();
                groupDBEntity.setGroupId(groupId.intValue());
                groupDBEntity.setBoardId(boardId);
                groupDBEntity.setTitle(groupTuple.getValue1());
                List<TaskDBEntityWithParticipantsAddresses> taskDBEntities = new ArrayList<>();
                Log.e(LOG_TAG, "Tasks size " + groupTuple.getValue2().size());
                for (BigInteger taskId : groupTuple.getValue2()) {
                    TaskDBEntity taskDBEntity = new TaskDBEntity();
                    Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, List<String>> taskTuple = proManSmartContract.getTask(taskId).send();
                    taskDBEntity.setTaskId(taskId.intValue());
                    taskDBEntity.setTitle(taskTuple.getValue1());
                    taskDBEntity.setDescription(taskTuple.getValue2());
                    taskDBEntity.setStart(Repository.REPOSITORY.numToCalendar(taskTuple.getValue3()));
                    taskDBEntity.setFinish(Repository.REPOSITORY.numToCalendar(taskTuple.getValue4()));
                    taskDBEntity.setGroupId(groupId.intValue());
                    taskDBEntity.setBoardId(boardId);
                    TaskDBEntityWithParticipantsAddresses taskDBEntityWithParticipantsAddresses = new TaskDBEntityWithParticipantsAddresses();
                    taskDBEntityWithParticipantsAddresses.setTaskDBEntity(taskDBEntity);
                    taskDBEntityWithParticipantsAddresses.setParticipants(taskTuple.getValue7());
                    taskDBEntities.add(taskDBEntityWithParticipantsAddresses);
                }
                BoardGroup boardGroup = new BoardGroup();
                boardGroup.setGroupDBEntity(groupDBEntity);
                boardGroup.setTasks(taskDBEntities);
                groupsWithTasks.add(boardGroup);
            }
            board.setBoardGroups(groupsWithTasks);
            List<ParticipantDBEntity> participantDBEntities = getParticipants(tuple.getValue5());
            board.setParticipants(participantDBEntities);
            Log.e(LOG_TAG, "Succeeded loading board");
            return board;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed loading board");
            return null;
        }
    }

    String addBoardParticipant(int boardId, String address) {
        try {
            TransactionReceipt tx = proManSmartContract.addBoardParticipant(BigInteger.valueOf(boardId), address).send();
            List<ProManSmartContract.BoardParticipantAddedEventResponse> nicks = proManSmartContract.getBoardParticipantAddedEvents(tx);
            if (nicks.size() > 0 && !nicks.get(0).nick.equals("")) {
                Log.e(LOG_TAG, "Succeeded adding board participant");
                return nicks.get(0).nick;
            }
            Log.e(LOG_TAG, "Failed adding board participant");
            return null;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed adding board participant");
            return null;
        }
    }

    String addTaskParticipant(int taskId, String address) {
        try {
            TransactionReceipt tx = proManSmartContract.addTaskParticipant(BigInteger.valueOf(taskId), address).send();
            List<ProManSmartContract.TaskParticipantAddedEventResponse> nicks = proManSmartContract.getTaskParticipantAddedEvents(tx);
            if (nicks.size() > 0 && !nicks.get(0).nick.equals("")) {
                Log.e(LOG_TAG, "Succeeded adding task participant");
                return nicks.get(0).nick;
            }
            Log.e(LOG_TAG, "Failed adding task participant");
            return null;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed adding task participant");
            return null;
        }
    }

    TransactionReceipt removeTaskParticipant(int taskId, String address) {
        try {
            Log.e(LOG_TAG, "Succeeded removing task participant");
            return proManSmartContract.removeTaskUser(BigInteger.valueOf(taskId), address).send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed removing task participant");
            return null;
        }
    }

    List<BoardDBEntity> getBoards() {
        try {
            List<BigInteger> boards = proManSmartContract.getBoardsIndices().send();
            List<BoardDBEntity> boardDBEntities = new ArrayList<>();
            Log.e(LOG_TAG, "boards size " + boards.size());
            for (BigInteger id: boards) {
                Tuple3<String, BigInteger, BigInteger> tuple =
                        proManSmartContract.getBoardCard(id).send();
                BoardDBEntity boardDBEntity = new BoardDBEntity();
                boardDBEntity.setBoardId(id.intValue());
                boardDBEntity.setTitle(tuple.getValue1());
                Calendar start = Repository.REPOSITORY.numToCalendar(tuple.getValue2());
                Calendar finish = Repository.REPOSITORY.numToCalendar(tuple.getValue3());
                boardDBEntity.setStart(start);
                boardDBEntity.setFinish(finish);
                boardDBEntities.add(boardDBEntity);
            }
            Log.e(LOG_TAG, "Succeeded loading boards");
            return boardDBEntities;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed loading boards " + e.getMessage());
            return null;
        }
    }

    TransactionReceipt leaveBoard(int id) {
        try {
            TransactionReceipt tx = proManSmartContract.leaveBoard(BigInteger.valueOf(id)).send();
            Log.e(LOG_TAG, "Succeeded leaving board");
            return tx;
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed leaving board " + e.getMessage());
            return null;
        }
    }
}
