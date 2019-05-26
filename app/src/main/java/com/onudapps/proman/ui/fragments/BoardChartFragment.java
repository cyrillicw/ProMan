package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.GroupStatistic;
import com.onudapps.proman.viewmodels.BoardChartViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardChartFragment extends Fragment {

    private static final String BOARD_ID_TAG = "boardId";

    private int boardId;

    private PieChart groupsDistribution;

    public BoardChartFragment() {
        super();
    }

    public static BoardChartFragment newInstance(int boardId) {
        Bundle args = new Bundle();
        args.putInt(BOARD_ID_TAG, boardId);
        BoardChartFragment f = new BoardChartFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardId = getArguments().getInt(BOARD_ID_TAG);
        View view = inflater.inflate(R.layout.fragment_board_chart, container, false);
        groupsDistribution = view.findViewById(R.id.groups_distribution);
        BoardChartViewModel boardChartViewModel = ViewModelProviders
                .of(this, new BoardChartViewModel.BoardChartModelFactory(boardId))
                .get(BoardChartViewModel.class);
        LiveData<List<GroupStatistic>> groupData = boardChartViewModel.getGroupsData();
        groupData.observe(this, this::onGroupsChangedListener );
//        DataSet
//        PieData pieData = new PieData();
//        pieData.s
//        groupsDistribution.setData();
        return view;
    }

    private void onGroupsChangedListener(List<GroupStatistic> groupStatistics) {
        Log.e("CHART", "STAT " + groupStatistics.size());
        List<PieEntry> yValues = new ArrayList<>();
        for (GroupStatistic groupStatistic : groupStatistics) {
            yValues.add(new PieEntry(groupStatistic.getTasksCount(), groupStatistic.getTitle()));

        }
        PieDataSet pieDataSet = new PieDataSet(yValues, "groups");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setDrawValues(false);
        groupsDistribution.setData(pieData);
        groupsDistribution.setTouchEnabled(false);
        Description description = new Description();
        description.setText("GROUPS DISTRIBUTION");
        groupsDistribution.setDescription(description);
        groupsDistribution.notifyDataSetChanged();
        groupsDistribution.invalidate();
        // groupsDistribution.setLayoutParams(new FrameLayout.LayoutParams(width, width));
    }
}
