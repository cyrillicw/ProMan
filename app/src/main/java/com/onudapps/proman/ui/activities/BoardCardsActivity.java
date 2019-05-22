package com.onudapps.proman.ui.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.ui.adapters.BoardsRecyclerAdapter;
import com.onudapps.proman.viewmodels.BoardCardsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardCardsActivity extends AppCompatActivity implements Observer<List<BoardCard>> {
    private static final String LOG_TAG = "BOARDCARDS ACTIVITY";
    private static final String APP_NAME = "PROMAN";

    private Calendar lastUpdate;

    private RecyclerView recyclerView;
    private ProManSmartContractDeclaration contract;
    private boolean dialogOpened = true;
    private BoardCardsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_cards);
        SharedPreferences sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(BoardCardsViewModel.class);
        LiveData<Calendar> lastUpdateData = viewModel.getLastUpdateData();
        lastUpdateData.observe(this, calendar -> this.lastUpdate = calendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView createBoard = toolbar.findViewById(R.id.create);
        createBoard.setOnClickListener(this::createBoardListener);
        ImageView update = toolbar.findViewById(R.id.update);
        update.setOnClickListener(this::updateOnClickListener);
        recyclerView = findViewById(R.id.recycle_boards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardsRecyclerAdapter(new ArrayList<>(), viewModel));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LiveData<List<BoardCard>> boardsData = viewModel.getBoardsData();
        boardsData.observe(this, this::onBoardsDataChangeListener);
    }

    @Override
    public void onChanged(List<BoardCard> boardCards) {
        ((BoardsRecyclerAdapter) recyclerView.getAdapter()).updateData(boardCards);
    }

    private void onBoardsDataChangeListener(List<BoardCard> boardCards) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (lastUpdate == null || lastUpdate.before(threshold)) {
            viewModel.forceBoardsUpdate();
        }
        else {
            ((BoardsRecyclerAdapter)recyclerView.getAdapter()).updateData(boardCards);
        }
    }

    private void updateOnClickListener(View v) {
        viewModel.forceBoardsUpdate();
    }

    private void createBoardListener(View v) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.alert_create).
                setPositiveButton(R.string.ok, null).create();
        alertDialog.show();
        TextView textView = alertDialog.findViewById(R.id.create_hint);
        textView.setText(R.string.create_board);
        final EditText boardTitle = alertDialog.findViewById(R.id.created_title);
        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setOnClickListener(b -> {
            String title = boardTitle.getText().toString();
            Repository.REPOSITORY.createBoard(title);
            alertDialog.dismiss();
        });
    }
}

