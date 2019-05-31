package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.onudapps.proman.R;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.adapters.BoardPagerAdapter;
import com.onudapps.proman.ui.dialog_fragments.CreateDialogFragment;
import com.onudapps.proman.viewmodels.BoardViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements CreateDialogListener{
    private static final String LOG_TAG = "BoardActivity";

    public static final String BOARD_KEY = "boardId";
    private static final int OK = 200;
    private ProManSmartContractDeclaration contract;
    private String privateKey;

    private int boardId;
    private BoardViewModel viewModel;
    private LiveData<List<GroupWithUpdate>> groupsData;
    private LiveData<String> titleData;

    private ViewPager viewPager;
    private TextView title;
    private Toolbar toolbar;
    private ImageView groupsMode;
    private ImageView statisticsMode;
    private LinearLayout viewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        Intent intent = getIntent();
        boardId = intent.getIntExtra(BOARD_KEY, -1);
//        if (boardId == -1) {
//             boardId = savedInstanceState.getInt(BOARD_KEY);
//        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = findViewById(R.id.headline);
        viewSwitcher = findViewById(R.id.modes_layout);
        viewModel = ViewModelProviders
                .of(this, new BoardViewModel.BoardModelFactory(boardId))
                .get(BoardViewModel.class);
        ImageView createGroup = toolbar.findViewById(R.id.create);
        createGroup.setOnClickListener(this::createGroupListener);
        ImageView update = toolbar.findViewById(R.id.update);
        update.setOnClickListener(this::updateOnClickListener);
        viewPager = findViewById(R.id.board_pager);
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new ArrayList<>(), boardId));
        groupsData = viewModel.getGroupsData();
        groupsData.observe(this, this::onGroupsChangedListener);
        titleData = viewModel.getTitleData();
        titleData.observe(this, s -> title.setText(s));
//        title.setOnClickListener(v -> {
//            new BoardEditDialogFragment().show(getSupportFragmentManager(), "CREATE BOARD");
//        });
        groupsMode = findViewById(R.id.mode_groups);
        groupsMode.setOnClickListener(v -> viewPager.setCurrentItem(0, false));
        statisticsMode = findViewById(R.id.mode_statistics);
        statisticsMode.setOnClickListener(v -> viewPager.setCurrentItem(((BoardPagerAdapter)viewPager.getAdapter()).getStatisticModePosition(), false));
        ImageView propertiesMode = findViewById(R.id.mode_properties);
        propertiesMode.setOnClickListener(v -> viewPager.setCurrentItem(((BoardPagerAdapter)viewPager.getAdapter()).getPropertiesModePosition(), false));
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
        CreateDialogFragment createGroup = CreateDialogFragment.newInstance(getResources().getString(R.string.create_group));
        createGroup.show(getSupportFragmentManager(), "Create group");
    }

    public BoardViewModel getViewModel() {
        return viewModel;
    }

    private void onGroupsChangedListener(List<GroupWithUpdate> groups) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (groups.size() == 0 || groups.get(0).getUpdated().before(threshold)) {
            viewModel.forceBoardUpdate();
        }
        if (groups.size() != 0 && groups.get(0).getGroupDBEntity() != null) {
            Log.e(LOG_TAG, "NOW");
            statisticsMode.setVisibility(View.VISIBLE);
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(groups);
            viewPager.invalidate();
        }
        else {
            statisticsMode.setVisibility(View.GONE);
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(new ArrayList<>());
            viewPager.invalidate();
        }
    }

    @Override
    public void onCreateCommit(String res) {
        viewModel.createGroup(res);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(LOG_TAG, "ON SAVED INSTANCE");
        outState.putInt(BOARD_KEY, boardId);
    }
}
