package com.onudapps.proman.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.onudapps.proman.data.pojo.GroupWithUpdate;
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

    private BoardViewModel viewModel;
    private LiveData<List<GroupWithUpdate>> groupsData;
    private LiveData<String> titleData;

    private ViewPager viewPager;
    private TextView title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        Intent intent = getIntent();
        int boardId = intent.getIntExtra(BOARD_KEY, -1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = findViewById(R.id.headline);
        viewModel = ViewModelProviders
                .of(this, new BoardViewModel.BoardModelFactory(boardId))
                .get(BoardViewModel.class);
        ImageView createGroup = toolbar.findViewById(R.id.create);
        createGroup.setOnClickListener(this::createGroupListener);
        ImageView update = toolbar.findViewById(R.id.update);
        update.setOnClickListener(this::updateOnClickListener);
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new ArrayList<>()));
        groupsData = viewModel.getGroupsData();
        groupsData.observe(this, this::onGroupsChangedListener);
        titleData = viewModel.getTitleData();
        titleData.observe(this, s -> title.setText(s));
    }

    public LiveData<List<GroupWithUpdate>> getGroupsData() {
        return groupsData;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateOnClickListener(View v) {
        viewModel.forceBoardUpdate();
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

    private void onGroupsChangedListener(List<GroupWithUpdate> groups) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (groups.size() == 0 || groups.get(0).getUpdated().before(threshold)) {
            viewModel.forceBoardUpdate();
        }
        else {
            Log.e(LOG_TAG, "NOW");
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(groups);
        }
    }
}
