package com.onudapps.proman.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.data.pojo.Task;
import io.reactivex.Flowable;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.List;
import java.util.UUID;

public enum  Repository {
    REPOSITORY;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public static void initialize(Context context) {
        REPOSITORY.localDataSource = new LocalDataSource(context);
        REPOSITORY.remoteDataSource = new RemoteDataSource();
    }

    public Task getTask(UUID id) {
        return localDataSource.getTask(id);
    }

    public Flowable<TransactionReceipt> addBoard(BoardCard boardCard) {
        return remoteDataSource.addBoard(boardCard);
    }

    public LiveData<List<BoardCard>> getBoardCards() {
        return localDataSource.getBoardCards();
    }

    public void createBoard(String title) {
        Flowable<TransactionReceipt> transactionReceiptFlowable = remoteDataSource.createBoard(title);
        transactionReceiptFlowable.subscribe(tx -> {
            Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
            BoardCard boardCard = new BoardCard();
            BoardDBEntity boardDBEntity = new BoardDBEntity();
            boardDBEntity.setBoardId(id);
            boardDBEntity.setTitle(title);
            boardDBEntity.setStart(null);
            boardDBEntity.setFinish(null);
            localDataSource.insertBoardCard(boardCard);
        });
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
