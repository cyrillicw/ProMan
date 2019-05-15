package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.adapters.BoardPagerAdapter;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.pojo.Board;

public class BoardActivity extends AppCompatActivity {
    private static final String LOG_TAG = "BoardActivity";

    public static final String BOARD_KEY = "boardId";
    private static final int OK = 200;
    private ProManSmartContractDeclaration contract;
    private String privateKey;


    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        Intent intent = getIntent();
        String boardId = intent.getStringExtra(BOARD_KEY);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        //getBoard(boardTitle);
        /*Board board = new Board();
        List<BoardGroup> boardGroups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BoardGroup boardGroup = new BoardGroup();
            List<Task> tasks = new ArrayList<>();
            for (int j = 0; j < 100; j ++) {
                Task task = new Task();
                task.setTitle("T " + j);
                String[] parts = {"P1", "P2"};
                task.setParticipants(parts);
                tasks.add(task);
            }
            boardGroup.setTitle("G " + i);
            boardGroup.setTasks(tasks);
            boardGroups.add(boardGroup);
        }
        board.setTitle("B");
        board.setBoardGroups(boardGroups);*/
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new Board()));
        contract.getBoard(boardId, privateKey).sendAsync().thenAccept(board -> {
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(board);
        }).exceptionally(throwable -> {
            Log.e(LOG_TAG, throwable.getMessage());
            return null;
        });
//        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new Board()));
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("?")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        BoardService boardService = retrofit.create(BoardService.class);
//        boardService.getBoardById(boardId).enqueue(new BoardCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
