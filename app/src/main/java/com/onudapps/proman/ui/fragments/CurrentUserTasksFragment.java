package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.TaskCard;
import com.onudapps.proman.ui.adapters.TasksRecyclerAdapter;
import com.onudapps.proman.viewmodels.CurrentUserTasksViewModel;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserTasksFragment extends Fragment {
    private static final String BOARD_ID_TAG = "boardId";
    public static CurrentUserTasksFragment newInstance(int boardId) {
        Bundle args = new Bundle();
        args.putInt(BOARD_ID_TAG, boardId);
        CurrentUserTasksFragment f = new CurrentUserTasksFragment();
        f.setArguments(args);
        return f;
    }
    private RecyclerView tasksRecycler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board_group, container, false);
        int boardId = getArguments().getInt(BOARD_ID_TAG);
        TextView title = view.findViewById(R.id.group_title);
        title.setText(R.string.current_user_tasks_title);
        tasksRecycler = view.findViewById(R.id.recycler_board_group);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        tasksRecycler.setLayoutManager(layoutManager);
        tasksRecycler .setAdapter(new TasksRecyclerAdapter(boardId, new ArrayList<>()));
        CardView addTask = view.findViewById(R.id.add_task_card);
        addTask.setVisibility(View.GONE);
        CurrentUserTasksViewModel viewModel = ViewModelProviders
                .of(this, new CurrentUserTasksViewModel.CurrentUserTasksModelFactory(boardId))
                .get(CurrentUserTasksViewModel.class);
        LiveData<List<TaskCard>> tasksData = viewModel.getTasksData();
        tasksData.observe(this, this::onTasksDataChanged);
        return view;
    }

    private void onTasksDataChanged(List<TaskCard> taskCards) {
        ((TasksRecyclerAdapter)tasksRecycler.getAdapter()).updateData(taskCards);
    }
}
