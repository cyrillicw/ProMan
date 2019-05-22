package com.onudapps.proman.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.onudapps.proman.R;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.ui.adapters.BoardPagerAdapter;
import com.onudapps.proman.viewmodels.BoardViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private static final String LOG_TAG = "BoardActivity";

    public static final String BOARD_KEY = "boardId";
    private static final int OK = 200;
    private ProManSmartContractDeclaration contract;
    private String privateKey;
    private Calendar lastUpdate;
    private BoardViewModel viewModel;


    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        Intent intent = getIntent();
        int boardId = intent.getIntExtra(BOARD_KEY, -1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewModel = ViewModelProviders
                .of(this, new BoardViewModel.BoardModelFactory(boardId))
                .get(BoardViewModel.class);
        LiveData<List<GroupDBEntity>> data = viewModel.getGroupsData();
        data.observe(this, this::onGroupsChangedListener);
        LiveData<Calendar> lastUpdateData = viewModel.getLastUpdateData();
        lastUpdateData.observe(this, calendar -> this.lastUpdate = calendar);
        ImageView createGroup = toolbar.findViewById(R.id.create);
        createGroup.setOnClickListener(this::createGroupListener);
        ImageView update = toolbar.findViewById(R.id.update);
        // update.setOnClickListener(this::updateOnClickListener);
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
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new ArrayList<>()));

//        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new Board()));
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("?")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        BoardService boardService = retrofit.create(BoardService.class);
//        boardService.getBoardById(boardId).enqueue(new BoardCallback());
    }

    private void createGroupListener(View v) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.alert_create).
                setPositiveButton(R.string.ok, null).create();
        alertDialog.show();
        TextView textView = alertDialog.findViewById(R.id.create_hint);
        textView.setText(R.string.create_group);
        final EditText groupTitle = alertDialog.findViewById(R.id.created_title);
        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setOnClickListener(b -> {
            String title = groupTitle.getText().toString();
            viewModel.createGroup(title);
            alertDialog.dismiss();
        });
    }

    private void onGroupsChangedListener(List<GroupDBEntity> groupDBEntities) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (lastUpdate == null || lastUpdate.before(threshold)) {
            viewModel.forceBoardUpdate();
        }
        else {
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(groupDBEntities);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
