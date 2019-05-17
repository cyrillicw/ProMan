package com.onudapps.proman.ui.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.ui.adapters.BoardsRecyclerAdapter;
import io.reactivex.Flowable;
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
    //private MutableLiveData<List<BoardCard>> boardsLive;
    private RecyclerView recyclerView;
    private ProManSmartContractDeclaration contract;
    private LiveData<List<BoardCard>> data;
    private boolean dialogOpened = true;

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
        data = Repository.REPOSITORY.getBoardCards();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final ImageView createBoard = toolbar.findViewById(R.id.create_board);
        createBoard.setOnClickListener(this::createBoardListener);
        recyclerView = findViewById(R.id.recycle_boards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardsRecyclerAdapter(new ArrayList<>()));
        data.observe(this, l -> {
            ((BoardsRecyclerAdapter)recyclerView.getAdapter()).updateData(l);
        });
        // boardsLive = new MutableLiveData<>();
        // refresh();
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
                boardCard.setStart(start);
                boardCard.setFinish(finish);
                Flowable<TransactionReceipt> flowable = Repository.REPOSITORY.addBoard(boardCard);
                flowable.subscribe(tx -> {
                    Integer id = Integer.parseInt(tx.getLogs().get(0).getData().substring(2), 16);
                    boardCard.setBoardId(id);
                    // Log.e("ADDED", id);
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

    /*
        Как сохранять состояние диалога
     */

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void createBoardListener(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.alert_create).
                setPositiveButton(R.string.ok, null).create();
        alertDialog.show();
        final EditText boardTitle = alertDialog.findViewById(R.id.created_board_title);
        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setOnClickListener(b -> {
            String title = boardTitle.getText().toString();
            Repository.REPOSITORY.createBoard(title);
        });
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

//    private void getBoards() {
//        CompletableFuture<List<BoardCard>> completableFuture = contract.getBoards(privateKey).sendAsync();
//        completableFuture.thenAccept(boards -> {
//            boardsLive.postValue(boards);
//        }).exceptionally(throwable -> {
//            Log.e(LOG_TAG, "ERROR LOADING CARDS" + throwable.getMessage());
//            return null;
//        });
//    }
}

