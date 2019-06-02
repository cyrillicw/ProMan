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

import java.util.List;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TaskViewHolder> {
    private List<TaskCard> tasks;

    public TasksRecyclerAdapter(List<TaskCard> tasks) {
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
        holder.bindData(position);
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

        private void bindData(int position) {
            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                intent.putExtra(TaskActivity.taskIdTag, tasks.get(position).getTaskId());
                v.getContext().startActivity(intent);
            });
            view.setOnLongClickListener(v -> {
                BoardActivity boardActivity = (BoardActivity) v.getContext();
                List<GroupWithUpdate> groups = boardActivity.getGroups();
                String[] groupsTitles = new String[groups.size()];
                int[] groupsIds = new int[groups.size()];
                for (int i = 0; i < groups.size(); i++) {
                    groupsTitles[i] = groups.get(i).getGroupDBEntity().getTitle();
                    groupsIds[i] = groups.get(i).getGroupDBEntity().getGroupId();
                }
                TaskChangeGroupDialogFragment.newInstance(groupsTitles, groupsIds, tasks.get(position).getTaskId()).show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), null);
                return true;
            });
            title.setText(tasks.get(position).getTitle());
        }
    }
}
