package com.onudapps.proman.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.onudapps.proman.R;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.pojo.BoardWithUpdate;
import com.onudapps.proman.ui.activities.BoardActivity;
import com.onudapps.proman.ui.dialog_fragments.BoardsMenuDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardsRecyclerAdapter extends RecyclerView.Adapter<BoardsRecyclerAdapter.BoardViewHolder> {
    private static final String LOG_TAG = "BoardsRecyclerAdapter";

    private List<BoardWithUpdate> boards;

    public BoardsRecyclerAdapter(List<BoardWithUpdate> boards) {
        this.boards = boards;
    }

    public void updateData(List<BoardWithUpdate> boards) {
        this.boards = boards;
        notifyDataSetChanged();
    }

//    public void addBoardCard(BoardCard boardCard) {
//        boards.add(boardCard);
//        notifyItemInserted(boards.size() - 1);
//    }

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
        TextView motivation;
//        TextView participantsCount;
//        TextView participants;
        private BoardViewHolder(final View view) {
            super(view);
            title = view.findViewById(R.id.board_title);
            barChart = view.findViewById(R.id.chart);
            motivation = view.findViewById(R.id.motivation);
//            participantsCount = view.findViewById(R.id.participants_count);
//            participants = view.findViewById(R.id.participants);
            this.view = view;
        }

        private void bindData(int position) {
            final BoardDBEntity board = boards.get(position).getBoardDBEntity();
            title.setText(board.getTitle());
            drawChart(board);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), BoardActivity.class);
                    intent.putExtra(BoardActivity.BOARD_KEY, board.getBoardId());
                    view.getContext().startActivity(intent);
                }
            });
            view.setOnLongClickListener(v -> {
                BoardsMenuDialogFragment.newInstance(board.getBoardId()).show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), null);
                return true;
            });
        }

        private void drawChart(BoardDBEntity board) {
            Calendar start = board.getStart();
            Calendar finish = board.getFinish();
            if (start != null && finish != null && start.before(finish)) {
                barChart.setVisibility(View.VISIBLE);
                motivation.setVisibility(View.GONE);
                Calendar current = Calendar.getInstance();
                int finished;
                if (finish.before(current)) {
                    finished = 100;
                }
                else if (!start.before(current)) {
                    finished = 0;
                }
                else {
                    finished = Math.min(100, (int) ((double) (current.getTimeInMillis() - start.getTimeInMillis()) / (finish.getTimeInMillis() - board.getStart().getTimeInMillis()) * 100));
                }
                List<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(1, finished));
                BarDataSet barDataSet = new BarDataSet(barEntries, "E");
                barDataSet.setValueTextSize(30);
                int color;
                if (finished == 100) {
                    color = colors[colors.length - 1];
                }
                else {
                    color = colors[Math.min(colors.length - 1, (int) (colors.length * (double) finished / 100))];
                }
                barDataSet.setColor(color, 0xFF);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                Description description = new Description();
                description.setText("");
                barChart.setDescription(description);    // Hide the description
                barChart.getLegend().setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.getAxisLeft().setAxisMaximum(100);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getXAxis().setDrawLabels(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawLabels(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisRight().setDrawLabels(false);
                barChart.setTouchEnabled(false);
                barDataSet.setDrawValues(false);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }
            else {
                barChart.setVisibility(View.GONE);
                String[] motivations = view.getResources().getStringArray(R.array.motivation_array);
                String motivationText = motivations[(int)(Math.random() * motivations.length)];
                motivation.setText(motivationText);
                motivation.setVisibility(View.VISIBLE);
            }
        }
    }
}
