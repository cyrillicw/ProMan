package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
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
    private LinearLayout availableGantt;
    private TextView unavailableGantt;
    private LinearLayout availableGroupsDistribution;
    private TextView unavailableGroupsDistribution;


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
        availableGroupsDistribution = view.findViewById(R.id.available_groups_distribution);
        unavailableGroupsDistribution = view.findViewById(R.id.unavailable_groups_distribution);
        availableGantt = view.findViewById(R.id.available_gantt);
        unavailableGantt = view.findViewById(R.id.unavailable_gantt);
        taskCalendarData = boardChartViewModel.getTasksCalendarData();
        taskCalendarData.observe(this, this::onTaskCalendarChangeListener);
//        DataSet
//        PieData pieData = new PieData();
//        pieData.s
//        groupsDistribution.setData();
        return view;
    }

    private void onTaskCalendarChangeListener(List<TaskCalendarCard> taskCalendarCards) {
        Log.e("CHART GANTT", "HERE");
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
                float dStart;
                if (currentStart.before(start)) {
                    dStart = 1;
                } else {
                    dStart = currentStart.get(Calendar.DAY_OF_MONTH);
                    dStart += dayPart(currentStart);
                }
                float dFinish;
                if (currentFinish.after(finish)) {
                    dFinish = maxDays;
                } else {
                    dFinish = currentFinish.get(Calendar.DAY_OF_MONTH);
                    dFinish += dayPart(currentFinish);
                }
                dStart -= 1;
                dFinish -= 1;
                //Log.e("STARTS", ": " + dStart + " " + dFinish);
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
        if (barEntries.size() > 0) {
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
            gantt.getAxisLeft().setAxisMinimum(0);
            gantt.getAxisLeft().setAxisMaximum(maxDays);
            gantt.getAxisRight().setAxisMinimum(0);
            gantt.getAxisRight().setAxisMaximum(maxDays);
            //gantt.getAxisLeft().setDrawTopYLabelEntry(false);
            gantt.getAxisLeft().setDrawGridLines(false);
            List<String> yLabels = new ArrayList<>();
            Log.e("MAX", "DAYS " + maxDays);
            for (int i = 0; i < maxDays; i++) {
                yLabels.add(Integer.toString(i + 1));
            }
//            Log.e("GANTT", "WIDTH" + gantt.getWidth());
            gantt.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(yLabels));
            gantt.getAxisRight().setValueFormatter(new IndexAxisValueFormatter(yLabels));
            gantt.getAxisRight().setLabelCount(yLabels.size() / 2);
            gantt.getAxisLeft().setLabelCount(yLabels.size() / 2);
            gantt.getAxisLeft().setDrawAxisLine(false);
            gantt.getXAxis().setDrawGridLines(false);
            gantt.getXAxis().setDrawAxisLine(false);
            gantt.getXAxis().disableGridDashedLine();
            gantt.setData(barData);
            XAxis xAxis = gantt.getXAxis();
            Log.e("CHART", "xSize " + xAxisLabel.size());
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
            xAxis.setLabelCount(xAxisLabel.size());
            gantt.setTouchEnabled(false);
            gantt.notifyDataSetChanged();
            gantt.setDescription(null);
            gantt.getLegend().setEnabled(false);
            float height = getResources().getDimension(R.dimen.chart_column_height);
            gantt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * (barEntries.size() + 1))));
            unavailableGantt.setVisibility(View.GONE);
            availableGantt.setVisibility(View.VISIBLE);
            gantt.invalidate();
        }
        else {
            unavailableGantt.setVisibility(View.VISIBLE);
            availableGantt.setVisibility(View.GONE);
        }

//        HorizontalBarChart horizontalBarChart = new HorizontalBarChart()
//        RangeBar rangeBar = new RangeBar("J");
//        Range
//        rangeBar.
    }

    private void onGroupsChangedListener(List<GroupStatistic> groupStatistics) {
        Log.e("CHART", "STAT GROUP");
        List<PieEntry> yValues = new ArrayList<>();
        int tasksCount = 0;
        for (GroupStatistic groupStatistic : groupStatistics) {
            tasksCount += groupStatistic.getTasksCount();
            yValues.add(new PieEntry(groupStatistic.getTasksCount(), groupStatistic.getTitle()));
        }
        if (tasksCount > 0) {
            PieDataSet pieDataSet = new PieDataSet(yValues, "groups");
            pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieDataSet.setDrawValues(false);
            groupsDistribution.setData(pieData);
            groupsDistribution.setTouchEnabled(false);
            groupsDistribution.setDescription(null);
            availableGroupsDistribution.setVisibility(View.VISIBLE);
            unavailableGroupsDistribution.setVisibility(View.GONE);
            groupsDistribution.notifyDataSetChanged();
            groupsDistribution.invalidate();
        }
        else {
            availableGroupsDistribution.setVisibility(View.GONE);
            unavailableGroupsDistribution.setVisibility(View.VISIBLE);
        }
        // groupsDistribution.setLayoutParams(new FrameLayout.LayoutParams(width, width));
    }

    private float dayPart(Calendar calendar) {
        return (float) ((double)(calendar.get(Calendar.HOUR_OF_DAY) * 60
                + calendar.get(Calendar.MINUTE)) / (24 * 60));
    }
}
