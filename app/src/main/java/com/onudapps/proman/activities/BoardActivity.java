package com.onudapps.proman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.onudapps.proman.R;
import com.onudapps.proman.adapters.BoardPagerAdapter;
import com.onudapps.proman.pojo.Board;
import com.onudapps.proman.pojo.BoardGroup;
import com.onudapps.proman.pojo.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    public static final String BOARD_KEY = "boardId";
    private static final int OK = 200;

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        try {
            Intent intent = getIntent();
            String boardId = intent.getStringExtra(BOARD_KEY);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        //getBoard(boardTitle);
        Board board = new Board();
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
        board.setBoardGroups(boardGroups);
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), board));
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

    private interface BoardService {
        @GET("")
        Call<Board> getBoardById(@Query("boardId")String boardId);
    }

    private class BoardCallback implements Callback<Board> {
        @Override
        public void onResponse(Call<Board> call, Response<Board> response) {
            if (response.isSuccessful() && response.code() == OK) {
                Board board = response.body();
                ((BoardPagerAdapter) viewPager.getAdapter()).updateData(board);
            }
        }

        @Override
        public void onFailure(Call<Board> call, Throwable t) {
        }
    }
}
