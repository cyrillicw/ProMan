package com.onudapps.proman.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.TaskCard;
import com.onudapps.proman.ui.adapters.TasksRecyclerAdapter;
import com.onudapps.proman.viewmodels.GroupViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardGroupFragment extends Fragment {
    // private BoardGroup boardGroup;
    private int groupId;
    private int boardId;
    private GroupViewModel groupViewModel;

    private TextView title;
    private RecyclerView recyclerView;
    private TextView addTask;
    private EditText addTaskEdit;
    private ImageView tick;
    private ImageView cross;
    private LinearLayout editLayout;

    public BoardGroupFragment() {
        super();
    }

    public static BoardGroupFragment newInstance(int groupId, int boardId) {
        Bundle args = new Bundle();
        args.putInt("groupId", groupId);
        args.putInt("boardId", boardId);
        BoardGroupFragment f = new BoardGroupFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupId = getArguments().getInt("groupId");
        boardId = getArguments().getInt("boardId");
        View view = inflater.inflate(R.layout.fragment_board_group, container, false);
        recyclerView = view.findViewById(R.id.recycler_board_group);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TasksRecyclerAdapter(boardId, new ArrayList<>()));
        groupViewModel = ViewModelProviders
                .of(this, new GroupViewModel.GroupModelFactory(groupId, boardId))
                .get(GroupViewModel.class);
        addTask = view.findViewById(R.id.add_task);
        addTaskEdit = view.findViewById(R.id.add_task_edit);
        title = view.findViewById(R.id.group_title);
//        tick = toolbar.findViewById(R.id.tick);
//        cross = toolbar.findViewById(R.id.cross);
        editLayout = view.findViewById(R.id.edit_layout);
        tick = view.findViewById(R.id.tick);
        cross = view.findViewById(R.id.cross);
        addTask.setOnClickListener(this::onAddTaskClickListener);
        cross.setOnClickListener(this::crossOnClickListener);
        tick.setOnClickListener(this::tickOnClickListener);
        LiveData<String> titleData = groupViewModel.getTitleData();
        titleData.observe(this, this::onTitleChangedListener);
        LiveData<List<TaskCard>> tasksData = groupViewModel.getTaskCardData();
        tasksData.observe(this, this::onTasksChangedListener);
        if (groupViewModel.isEditMode()) {
            enableEditMode();
        }
        return view;
    }

    private void crossOnClickListener(View v) {
        enableStandardMode();
    }

    private void enableStandardMode() {
        InputMethodManager imm = (InputMethodManager) addTaskEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addTaskEdit.getWindowToken(), 0);
        groupViewModel.setEditMode(false);
        groupViewModel.setText("");
        addTaskEdit.getText().clear();
        editLayout.setVisibility(View.VISIBLE);
        addTask.setVisibility(View.VISIBLE);
    }

    private void enableEditMode() {
        groupViewModel.setEditMode(true);
        addTask.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
    }

    private void onAddTaskClickListener(View v) {
        enableEditMode();
        // recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        addTaskEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) addTaskEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(addTaskEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private void tickOnClickListener(View v) {
        String title = addTaskEdit.getText().toString();
        Log.e("Fragment", "tick pressed");
        Log.e("Fragment", "title " + title);
        if (title.length() > 0) {
            groupViewModel.createTask(title);
            enableStandardMode();
        }
    }

    @Override
    public void onStop() {
        groupViewModel.setText(addTaskEdit.getText().toString());
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void onTitleChangedListener(String s) {
        title.setText(s);
    }

    private void onTasksChangedListener(List<TaskCard> taskCards) {
        ((TasksRecyclerAdapter)recyclerView.getAdapter()).updateData(taskCards);
    }
}
