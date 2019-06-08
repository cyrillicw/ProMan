package com.onudapps.proman.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.data.pojo.TaskCard;
import com.onudapps.proman.ui.activities.BoardActivity;
import com.onudapps.proman.ui.activities.TaskActivity;
import com.onudapps.proman.ui.dialog_fragments.TaskChangeGroupDialogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.onudapps.proman.ui.activities.TaskActivity.BOARD_ID_TAG;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TaskViewHolder> {
    private List<TaskCard> tasks;
    private int boardId;

    public TasksRecyclerAdapter(int boardId, List<TaskCard> tasks) {
        this.boardId = boardId;
        this.tasks = tasks;
    }

    public void updateData(List<TaskCard> taskCards) {
        this.tasks = taskCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TasksRecyclerAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindData(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        View view;
        private TaskViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.task_title);
        }

        private void bindData(TaskCard task) {
            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                intent.putExtra(TaskActivity.TASK_ID_TAG, task.getTaskId());
                intent.putExtra(BOARD_ID_TAG, boardId);
                v.getContext().startActivity(intent);
            });
            view.setOnLongClickListener(v -> {
                BoardActivity boardActivity = (BoardActivity) v.getContext();
                List<GroupWithUpdate> groups = boardActivity.getGroups();
                List<String> groupsTitles = new ArrayList<>();
                List<Integer> groupsIds = new ArrayList<>();
                for (int i = 0; i < groups.size(); i++) {
                    if (groups.get(i).getGroupDBEntity().getGroupId() != task.getGroupId()) {
                        groupsTitles.add(groups.get(i).getGroupDBEntity().getTitle());
                        groupsIds.add(groups.get(i).getGroupDBEntity().getGroupId());
                    }
                }
                int[] groupsIdsArray = new int[groupsIds.size()];
                for (int i = 0; i < groupsIds.size(); i++) {
                    groupsIdsArray[i] = groupsIds.get(i);
                }
                TaskChangeGroupDialogFragment.newInstance(groupsTitles.toArray(new String[0]), groupsIdsArray, task.getTaskId()).show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), null);
                return true;
            });
            title.setText(task.getTitle());
        }
    }
}
