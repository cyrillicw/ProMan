package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.contracts.Smart;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.List;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private Smart smartContract;
    public RemoteDataSource() {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.102:7545"));
        Credentials credentials = Credentials.create("28f3d307e639526a072b94cfa7f484ac84991118fbe7ac59cceb3abf53a58b67");
        smartContract = Smart.load("cb873726d48110661A45c43995b95167e5D44259", web3j, credentials, new DefaultGasProvider());
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

    public Tuple6<String, List<BigInteger>, List<String>, List<String>, List<BigInteger>, List<BigInteger>> getGroup(int groupId) {
        try {
            return smartContract.getGroup(BigInteger.valueOf(groupId)).send();
        }
        catch (Exception e) {
            return null;
        }
    }

    //Tuple3<String, List<BigInteger>, List<String>> res =

    public Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>> getBoards() {
        try {
            return smartContract.getBoardCards().send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Failed loading boards " + e.getMessage());
            return null;
        }
    }

    public Tuple4<String, BigInteger, BigInteger, List<BigInteger>> getBoard(int id) {
        try {
            return smartContract.getBoard(BigInteger.valueOf(id)).send();
        }
        catch (Exception e){
            return null;
        }
    }

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
