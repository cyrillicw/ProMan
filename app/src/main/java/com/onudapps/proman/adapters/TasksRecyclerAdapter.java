package com.onudapps.proman.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.pojo.Task;

import java.util.List;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TaskViewHolder> {
    private List<Task> tasks;

    public TasksRecyclerAdapter(List<Task> tasks) {
        this.tasks = tasks;
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
        private TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.task_title);
        }

        private void bindData(int position) {
            title.setText(tasks.get(position).getTitle());
        }
    }
}
