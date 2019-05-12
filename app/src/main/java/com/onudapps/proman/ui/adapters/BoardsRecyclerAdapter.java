package com.onudapps.proman.ui.adapters;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.activities.BoardActivity;
import com.onudapps.proman.data.entities.BoardCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardsRecyclerAdapter extends RecyclerView.Adapter<BoardsRecyclerAdapter.BoardViewHolder> {
    private List<BoardCard> boards;
    private LinearGradient linearGradient;

    public BoardsRecyclerAdapter(List<BoardCard> boards) {
        this.boards = boards;
    }

    public void updateData(List<BoardCard> boards) {
        this.boards = boards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    class BoardViewHolder extends RecyclerView.ViewHolder {
        private final int[] colors = {0x099609, 0xE3A509, 0xAB0A0A};
        View view;
        TextView title;
        BarChart barChart;
        TextView participantsCount;
        TextView participants;
        private BoardViewHolder(final View view) {
            super(view);
            title = view.findViewById(R.id.board_title);
            barChart = view.findViewById(R.id.chart);
            participantsCount = view.findViewById(R.id.participants_count);
            participants = view.findViewById(R.id.participants);
            this.view = view;
        }

        private void bindData(int position) {
            final BoardCard board = boards.get(position);
            //title.setText("HELLLOOOOOOOOOOOO");
            title.setText(board.getTitle());
            participantsCount.setText(Integer.toString(board.getParticipants()));
            participants.setText(itemView.getResources().getQuantityText(R.plurals.participants, board.getParticipants()));
            drawChart(board);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), BoardActivity.class);
                    intent.putExtra(BoardActivity.BOARD_KEY, board.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }

        private void drawChart(BoardCard board) {
            int finished = Math.max(100, (int)((double) (new Date().getTime() - board.getStartDate().getTime()) / (board.getFinishDate().getTime() - board.getStartDate().getTime()) * 100));
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(1, finished));
            BarDataSet barDataSet = new BarDataSet(barEntries, "E");
            barDataSet.setValueTextSize(30);
            int color = colors[Math.min(colors.length - 1, (int) (colors.length * (double)finished / 100))];
            barDataSet.setColor(color, 0xFF);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            Description description = new Description();
            description.setText("");
            barChart.setDescription(description);    // Hide the description
            barChart.getAxisLeft().setDrawLabels(false);
            barChart.getAxisRight().setDrawLabels(false);
            barChart.getXAxis().setDrawLabels(false);
            barChart.getLegend().setEnabled(false);
            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.getAxisLeft().setAxisMaximum(100);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.setTouchEnabled(false);
        }
    }
}
