package com.onudapps.proman.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardGroupFragment extends Fragment {
    // private BoardGroup boardGroup;
    private int groupId;
    private GroupViewModel groupViewModel;

    private TextView title;
    private RecyclerView recyclerView;

    public BoardGroupFragment(int groupId) {
        super();
        this.groupId = groupId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board_group, container, false);
        recyclerView = view.findViewById(R.id.recycler_board_group);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TasksRecyclerAdapter(new ArrayList<>()));
        groupViewModel = ViewModelProviders
                .of(this, new GroupViewModel.GroupModelFactory(groupId))
                .get(GroupViewModel.class);
        final Button button = view.findViewById(R.id.add_task);
        final EditText editText = view.findViewById(R.id.add_task_edit);
        title = view.findViewById(R.id.group_title);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                button.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LiveData<String> titleData = groupViewModel.getTitleData();
        titleData.observe(this, this::onTitleChangedListener);
        LiveData<List<TaskCard>> tasksData = groupViewModel.getTaskCardData();
        tasksData.observe(this, this::onTasksChangedListener);
    }

    private void onTitleChangedListener(String s) {
        title.setText(s);
    }

    private void onTasksChangedListener(List<TaskCard> taskCards) {
        ((TasksRecyclerAdapter)recyclerView.getAdapter()).updateData(taskCards);
    }
}
