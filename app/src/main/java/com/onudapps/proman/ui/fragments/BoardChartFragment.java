package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.GroupStatistic;
import com.onudapps.proman.data.pojo.TaskCalendarCard;
import com.onudapps.proman.viewmodels.BoardChartViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardChartFragment extends Fragment {

    private static final String BOARD_ID_TAG = "boardId";

    private int boardId;

    private PieChart groupsDistribution;
    private HorizontalBarChart gantt;

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
        LiveData<List<TaskCalendarCard>> taskCalendarData = null;
        gantt = view.findViewById(R.id.ganttChart);
        taskCalendarData = boardChartViewModel.getTasksCalendarData();
        taskCalendarData.observe(this, this::onTaskCalendarChangeListener);
//        DataSet
//        PieData pieData = new PieData();
//        pieData.s
//        groupsDistribution.setData();
        return view;
    }

    private void onTaskCalendarChangeListener(List<TaskCalendarCard> taskCalendarCards) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar finish = Calendar.getInstance();
        int maxDays = finish.getActualMaximum(Calendar.DAY_OF_MONTH);
        finish.set(Calendar.DAY_OF_MONTH, maxDays);
        finish.set(Calendar.HOUR_OF_DAY, 23);
        finish.set(Calendar.MINUTE, 59);
        finish.set(Calendar.SECOND, 59);
        float[] f = {5, 2};
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        List<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < taskCalendarCards.size(); i++) {
            int maxLength = 15;
            Calendar currentStart = taskCalendarCards.get(i).getStart();
            Calendar currentFinish = taskCalendarCards.get(i).getFinish();
            if (!currentStart.after(finish) && !currentFinish.before(start)) {
                int dStart;
                if (currentStart.before(start)) {
                    dStart = 1;
                } else {
                    dStart = currentStart.get(Calendar.DAY_OF_MONTH);
                }
                int dFinish;
                if (currentFinish.after(finish)) {
                    dFinish = maxDays;
                } else {
                    dFinish = currentFinish.get(Calendar.DAY_OF_MONTH);
                }
                barEntries.add(new BarEntry(i, new float[]{dStart, dFinish - dStart}));
                if (taskCalendarCards.get(i).getTitle().length() <= maxLength) {
                    xAxisLabel.add(taskCalendarCards.get(i).getTitle());
                }
                else {
                    String title = taskCalendarCards.get(i).getTitle().substring(0, maxLength);
                    title += "\u2026";
                    xAxisLabel.add(title);
                }

            }
        }
        //BarEntry barEntry1  = new BarEntry()
        BarDataSet barDataSet = new BarDataSet(barEntries, "LABEL");
        barDataSet.setDrawValues(false);
        int mainColor = ContextCompat.getColor(getContext(), R.color.chart);
        int chartColor = ContextCompat.getColor(getContext(), R.color.blank);
        barDataSet.setColors(chartColor, mainColor);
        BarData barData = new BarData(barDataSet);
        //gantt.getAxisLeft().setDrawLabels(false);
        //gantt.getAxisRight().setDrawLabels(false);
        //gantt.getXAxis().setDrawLabels(false);
        gantt.setDrawGridBackground(false);
        gantt.getAxisRight().disableGridDashedLine();
        //gantt.getAxisRight().setDrawTopYLabelEntry(false);
        gantt.getAxisRight().setDrawGridLines(false);
        gantt.getAxisRight().setDrawAxisLine(false);
        gantt.getAxisLeft().disableGridDashedLine();
        gantt.getAxisLeft().setAxisMinimum(1);
        gantt.getAxisLeft().setAxisMaximum(maxDays);
        gantt.getAxisRight().setAxisMinimum(1);
        gantt.getAxisRight().setAxisMaximum(maxDays);
        //gantt.getAxisLeft().setDrawTopYLabelEntry(false);
        gantt.getAxisLeft().setDrawGridLines(false);
        List<String> yLabels = new ArrayList<>();
        for (int i = 0; i <= maxDays; i++) {
            yLabels.add(Integer.toString(i));
        }
        gantt.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(yLabels));
        gantt.getAxisRight().setValueFormatter(new IndexAxisValueFormatter(yLabels));
        gantt.getAxisRight().setLabelCount(maxDays / 2);
        gantt.getAxisLeft().setLabelCount(maxDays / 2);
        gantt.getAxisLeft().setDrawAxisLine(false);
        gantt.getXAxis().setDrawGridLines(false);
        gantt.getXAxis().setDrawAxisLine(false);
        gantt.getXAxis().disableGridDashedLine();
        gantt.setData(barData);
        XAxis xAxis = gantt.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        xAxis.setLabelCount(xAxisLabel.size());
        gantt.setTouchEnabled(false);
        gantt.notifyDataSetChanged();
        gantt.setDescription(null);
        gantt.getLegend().setEnabled(false);
        gantt.invalidate();

//        HorizontalBarChart horizontalBarChart = new HorizontalBarChart()
//        RangeBar rangeBar = new RangeBar("J");
//        Range
//        rangeBar.
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
