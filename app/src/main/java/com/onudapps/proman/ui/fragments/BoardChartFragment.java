package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.activities.BoardActivity;
import com.onudapps.proman.viewmodels.BoardChartViewModel;
import com.onudapps.proman.viewmodels.BoardViewModel;

import java.util.List;

public class BoardChartFragment extends Fragment {

    private static final String BOARD_ID_TAG = "boardId";

    private int boardId;

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
        PieChart groupsDistribution = view.findViewById(R.id.groups_distribution);
        BoardChartViewModel boardChartViewModel = ViewModelProviders
                .of(this, new BoardChartViewModel.BoardChartModelFactory(boardId))
                .get(BoardChartViewModel.class);
        LiveData<List<GroupWithUpdate>> groupData = boardChartViewModel.getGroupsData();
        groupData.observe(this, onGroupsChangedListener );
        DataSet
        PieData pieData = new PieData();
        pieData.s
        groupsDistribution.setData();
        return view;
    }
}
