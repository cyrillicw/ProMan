package com.onudapps.proman.ui.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
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
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.pojo.BoardWithUpdate;
import com.onudapps.proman.ui.activities.BoardActivity;
import com.onudapps.proman.viewmodels.BoardCardsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BoardsRecyclerAdapter extends RecyclerView.Adapter<BoardsRecyclerAdapter.BoardViewHolder> {
    private static final String LOG_TAG = "BoardsRecyclerAdapter";

    private List<BoardWithUpdate> boards;
    private BoardCardsViewModel viewModel;

    public BoardsRecyclerAdapter(List<BoardWithUpdate> boards, BoardCardsViewModel viewModel) {
        this.boards = boards;
        this.viewModel = viewModel;
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
            //title.setText("HELLLOOOOOOOOOOOO");
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
                String[] options = {v.getResources().getString(R.string.leave_board),
                        v.getResources().getString(R.string.delete_board)};
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).setItems(options, (d, w) -> {
                    if (w == 0) {
                        viewModel.leaveBoard(board.getBoardId());
                    }
                }).create();
                alertDialog.show();
                return true;
            });
        }

        private void drawChart(BoardDBEntity board) {
            Calendar start = board.getStart();
            Calendar finish = board.getFinish();
            if (start != null && finish != null && !finish.before(start)) {
                barChart.setVisibility(View.VISIBLE);
                motivation.setVisibility(View.GONE);
                Calendar current = Calendar.getInstance();
                int finished = Math.min(100, (int) ((double) (finish.getTimeInMillis() - current.getTimeInMillis()) / (current.getTimeInMillis() - board.getStart().getTimeInMillis()) * 100));
                Log.e(LOG_TAG, "finished " + finished);
                List<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(1, finished));
                BarDataSet barDataSet = new BarDataSet(barEntries, "E");
                barDataSet.setValueTextSize(30);
                Log.e("TITLE", board.getTitle());
                Log.e("START", start.getTime().toString());
                Log.e("FINISH", finish.getTime().toString());
                int color = colors[Math.min(colors.length - 1, (int) (colors.length * (double) finished / 100))];
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
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.setTouchEnabled(false);
            }
            else {
                barChart.setVisibility(View.GONE);
                motivation.setVisibility(View.VISIBLE);
            }
        }
    }
}
