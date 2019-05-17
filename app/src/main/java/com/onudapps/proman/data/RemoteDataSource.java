package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.contracts.Smart;
import com.onudapps.proman.data.pojo.BoardCard;
import io.reactivex.Flowable;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private Smart smartContract;
    public RemoteDataSource() {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.102:7545"));
        Credentials credentials = Credentials.create("28f3d307e639526a072b94cfa7f484ac84991118fbe7ac59cceb3abf53a58b67");
        smartContract = Smart.load("5AcA536856993a5b1b270326d3172c92249933E3", web3j, credentials, new DefaultGasProvider());
    }

    public Flowable<TransactionReceipt> addBoard(BoardCard boardCard) {
        try {
            //TransactionReceipt transactionReceipt = smartContract.addBoard("gg", BigInteger.valueOf(0), BigInteger.valueOf(1)).send();
            return smartContract.addBoard(boardCard.getTitle(), BigInteger.valueOf(boardCard.getStart().getTimeInMillis()), BigInteger.valueOf(boardCard.getFinish().getTimeInMillis())).flowable();
//            Flowable<Smart.BoardAddedEventResponse> boardAddedEventResponseFlowable = smartContract.boardAddedEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
//            boardAddedEventResponseFlowable.subscribe(event -> {
//                Log.e(LOG_TAG, event.id.toString());
//            });
//            Log.e(LOG_TAG, "tx");
        }
        catch (Exception e){
            Log.e(LOG_TAG, "ERROR" + e.getMessage());
            return null;
        }
    }

    public Flowable<TransactionReceipt> createBoard(String title) {
        return smartContract.addBoard(title).flowable();
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
