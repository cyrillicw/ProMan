package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.pojo.BoardWithUpdate;
import com.onudapps.proman.ui.adapters.BoardsRecyclerAdapter;
import com.onudapps.proman.ui.dialog_fragments.CreateDialogFragment;
import com.onudapps.proman.ui.listeners.CreateDialogListener;
import com.onudapps.proman.ui.listeners.SignOutOnClickListener;
import com.onudapps.proman.viewmodels.BoardCardsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardCardsActivity extends AppCompatActivity implements CreateDialogListener {
    private RecyclerView recyclerView;
    private BoardCardsViewModel viewModel;
    private LiveData<List<BoardWithUpdate>> boardsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_cards);
        viewModel = ViewModelProviders.of(this).get(BoardCardsViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView createBoard = toolbar.findViewById(R.id.create);
        createBoard.setOnClickListener(this::createBoardListener);
        ImageView update = toolbar.findViewById(R.id.update);
        update.setOnClickListener(this::updateOnClickListener);
        ImageView signOut = toolbar.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new SignOutOnClickListener());
        recyclerView = findViewById(R.id.recycle_boards);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BoardsRecyclerAdapter(new ArrayList<>()));
        boardsData = viewModel.getBoardsData();
        boardsData.observe(this, this::onBoardsDataChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void onBoardsDataChangeListener(List<BoardWithUpdate> boardCards) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (boardCards.size() == 0 || boardCards.get(0).getUpdated().before(threshold)) {
            viewModel.forceBoardsUpdate();
        }
        if (boardCards.size() != 0 && boardCards.get(0).getBoardDBEntity() != null) {
            ((BoardsRecyclerAdapter) recyclerView.getAdapter()).updateData(boardCards);
        }
        else {
            ((BoardsRecyclerAdapter) recyclerView.getAdapter()).updateData(new ArrayList<>());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateOnClickListener(View v) {
        viewModel.forceBoardsUpdate();
    }

    private void createBoardListener(View v) {
        CreateDialogFragment createDialogFragment = CreateDialogFragment.newInstance(getResources().getString(R.string.create_board), getResources().getString(R.string.create_board_hint));
        createDialogFragment.show(getSupportFragmentManager(), "Create board");
//        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.alert_create).
//                setPositiveButton(R.string.ok, null).create();
//        alertDialog.show();
//        TextView textView = alertDialog.findViewById(R.id.create_hint);
//        textView.setText(R.string.create_board);
//        final EditText boardTitle = alertDialog.findViewById(R.id.created_title);
//        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        positive.setOnClickListener(b -> {
//            String title = boardTitle.getText().toString();
//
//            alertDialog.dismiss();
//        });
    }

    @Override
    public void onCreateCommit(String res) {
        Repository.REPOSITORY.createBoard(res);
    }
}

