package com.onudapps.proman.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.adapters.BoardsRecyclerAdapter;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.contracts.Smart;
import com.onudapps.proman.data.pojo.BoardCard;
import java8.util.concurrent.CompletableFuture;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        privateKey = sharedPreferences.getString(PRIVATE_KEY_PATTERN, null);
        Web3j web3j = Web3j.build(new HttpService(blockChainURL));
        recyclerView = findViewById(R.id.recycle_boards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardsRecyclerAdapter(new ArrayList<>()));
        boardsLive = new MutableLiveData<>();
        //contract = Smart.load(contractAddress, web3j, credentials, new DefaultGasProvider());
        try {Log.e("MAIN", Thread.currentThread().toString());
            Credentials credentials = Credentials.create("28f3d307e639526a072b94cfa7f484ac84991118fbe7ac59cceb3abf53a58b67");
            Smart smart = Smart.load(contractAddress, web3j, credentials, new DefaultGasProvider());
            CompletableFuture<BigInteger> completableFuture = smart.taskCount().sendAsync();//.get();
            completableFuture.thenAccept(count -> Log.e("THREAD", count.toString())).exceptionally(throwable -> {
                Log.e("EXCEPTION", Thread.currentThread().toString());
                return null;
            });
            //smart.createTask("HELLLLLOO").sendAsync().get();
            //Log.e("TaskCOUNT", bigInteger.toString());
        }
        catch (Exception e) {
            Log.e("M", "EEEERRRR");
            Log.e("M", e.getMessage());
        }
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

