package com.onudapps.proman.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.ui.adapters.BoardsRecyclerAdapter;
import io.reactivex.Flowable;
import java8.util.concurrent.CompletableFuture;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;

public class TempActivity extends AppCompatActivity implements Observer<List<BoardCard>> {
    private static final String LOG_TAG = "BOARDCARDS ACTIVITY";
    private static final String APP_NAME = "PROMAN";
    private static final String PRIVATE_KEY_PATTERN = "privateKey";
    private static final String PUBLIC_KEY_PATTERN = "publicKey";
    private String contractAddress;
    private String blockChainURL;
    private String privateKey;
    private String publicKey;
    private MutableLiveData<List<BoardCard>> boardsLive;
    private RecyclerView recyclerView;
    private ProManSmartContractDeclaration contract;

    @Override
    public void onChanged(List<BoardCard> boardCards) {
        ((BoardsRecyclerAdapter) recyclerView.getAdapter()).updateData(boardCards);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadProperies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        SharedPreferences sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        privateKey = sharedPreferences.getString(PRIVATE_KEY_PATTERN, null);
        recyclerView = findViewById(R.id.recycle_boards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardsRecyclerAdapter(new ArrayList<>()));
        boardsLive = new MutableLiveData<>();
        refresh();
        //contract = Smart.load(contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    private void refresh() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                BoardCard boardCard = new BoardCard();
                boardCard.setTitle("TITLE" + i);
                Calendar start = Calendar.getInstance();
                start.add(Calendar.DAY_OF_MONTH, -(random.nextInt() % 30));
                Calendar finish = Calendar.getInstance();
                finish.add(Calendar.DAY_OF_MONTH, random.nextInt() % 30);
                boardCard.setStartDate(start.getTime());
                boardCard.setFinishDate(finish.getTime());
                Flowable<TransactionReceipt> flowable = Repository.REPOSITORY.addBoard(boardCard);
                flowable.subscribe(tx -> {
                    String id = Integer.toString(Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16));
                    boardCard.setId(id);
                    Log.e("ADDED", id);
                    runOnUiThread(() -> {
                        ((BoardsRecyclerAdapter)recyclerView.getAdapter()).addBoardCard(boardCard);
                    });
                });
                try {
                    Thread.sleep(10000);
                }
                catch (Exception e) {}
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*private void getBoards() {
        int boardsCount = 10;
        List<BoardCard> boards = new ArrayList<>();
        for (int j = 0; j < boardsCount; j++) {
            BoardCard board = new BoardCard();
            board.setTitle("Board " + j);
            String[] parts = {"P1"};
            board.setParticipants(j);
            boards.add(board);
        }
        ((BoardsRecyclerAdapter) recyclerView.getAdapter()).updateData(boardCards);
    }*/

    private void loadProperies() {
        Properties properties = new Properties();
        try {
            properties.load(getBaseContext().getAssets().open("app.properties"));
            contractAddress = properties.getProperty("contractAddress");
            blockChainURL = properties.getProperty("blockChainURL");
        }catch (IOException e) {}

    }

    private void getBoards() {
        CompletableFuture<List<BoardCard>> completableFuture = contract.getBoards(privateKey).sendAsync();
        completableFuture.thenAccept(boards -> {
            boardsLive.postValue(boards);
        }).exceptionally(throwable -> {
            Log.e(LOG_TAG, "ERROR LOADING CARDS" + throwable.getMessage());
            return null;
        });
    }
}

