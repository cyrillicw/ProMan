package com.onudapps.proman.ui.activities;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.adapters.BoardPagerAdapter;
import com.onudapps.proman.ui.dialog_fragments.CreateDialogFragment;
import com.onudapps.proman.ui.listeners.CreateDialogListener;
import com.onudapps.proman.ui.listeners.SignOutOnClickListener;
import com.onudapps.proman.viewmodels.BoardViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements CreateDialogListener {
    private static final String LOG_TAG = "BoardActivity";

    public static final String BOARD_KEY = "boardId";
    private static final int OK = 200;

    private int boardId;
    private BoardViewModel viewModel;
    private LiveData<List<GroupWithUpdate>> groupsData;
    private LiveData<String> titleData;
    private List<GroupWithUpdate> groups;

    private ViewPager viewPager;
    private TextView title;
    private Toolbar toolbar;
    private ImageView groupsMode;
    private ImageView statisticsMode;
    private ImageView userTasksMode;
    private ImageView propertiesMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        viewPager = findViewById(R.id.board_pager);
        Intent intent = getIntent();
        boardId = intent.getIntExtra(BOARD_KEY, -1);
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
        ImageView signOut = toolbar.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new SignOutOnClickListener());
        groupsMode = findViewById(R.id.mode_groups);
        statisticsMode = findViewById(R.id.mode_statistics);
        propertiesMode = findViewById(R.id.mode_properties);
        userTasksMode = findViewById(R.id.mode_user);
        viewPager = findViewById(R.id.board_pager);
        viewPager.setAdapter(new BoardPagerAdapter(getSupportFragmentManager(), new ArrayList<>(), boardId));
        setModesHighlight(0);
        viewPager.addOnPageChangeListener(new BoardPageChangedListener());
        groupsData = viewModel.getGroupsData();
        groupsData.observe(this, this::onGroupsChangedListener);
        titleData = viewModel.getTitleData();
        titleData.observe(this, s -> title.setText(s));
        groupsMode.setOnClickListener(v -> viewPager.setCurrentItem(0, false));
        statisticsMode.setOnClickListener(v -> viewPager.setCurrentItem(((BoardPagerAdapter)viewPager.getAdapter()).getStatisticModePosition(), false));
        propertiesMode.setOnClickListener(v -> viewPager.setCurrentItem(((BoardPagerAdapter)viewPager.getAdapter()).getPropertiesModePosition(), false));
        userTasksMode.setOnClickListener(v -> viewPager.setCurrentItem(((BoardPagerAdapter)viewPager.getAdapter()).getUserModePosition(), false));
    }

    public static void setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    public static void setUnlocked(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }

    private void setModesHighlight(int position) {
        BoardPagerAdapter.BoardPagerMode mode = ((BoardPagerAdapter)viewPager.getAdapter()).getMode(position);
        setLocked(groupsMode);
        setLocked(propertiesMode);
        setLocked(statisticsMode);
        setLocked(userTasksMode);
        if (mode == BoardPagerAdapter.BoardPagerMode.GROUPS) {
            setUnlocked(groupsMode);
        }
        else if (mode == BoardPagerAdapter.BoardPagerMode.PROPERTIES) {
            setUnlocked(propertiesMode);
        }
        else if (mode == BoardPagerAdapter.BoardPagerMode.STATISTICS) {
            setUnlocked(statisticsMode);
        }
        else {
            setUnlocked(userTasksMode);
        }
    }

    public LiveData<List<GroupWithUpdate>> getGroupsData() {
        return groupsData;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public List<GroupWithUpdate> getGroups() {
        return groups;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateOnClickListener(View v) {
        viewModel.forceBoardUpdate();
    }

    private void createGroupListener(View v) {
        CreateDialogFragment createGroup = CreateDialogFragment.newInstance(getResources().getString(R.string.create_group), getResources().getString(R.string.create_group_hint));
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
            this.groups = groups;
            statisticsMode.setVisibility(View.VISIBLE);
            userTasksMode.setVisibility(View.VISIBLE);
            ((BoardPagerAdapter) viewPager.getAdapter()).updateData(groups);
            viewPager.invalidate();
        }
        else {
            this.groups = groups;
            statisticsMode.setVisibility(View.GONE);
            userTasksMode.setVisibility(View.GONE);
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
        outState.putInt(BOARD_KEY, boardId);
    }

    private class BoardPageChangedListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setModesHighlight(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
