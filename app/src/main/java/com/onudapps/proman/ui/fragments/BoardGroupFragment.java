package com.onudapps.proman.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.adapters.TasksRecyclerAdapter;
import com.onudapps.proman.data.pojo.BoardGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardGroupFragment extends Fragment {
    private BoardGroup boardGroup;
    public BoardGroupFragment(BoardGroup boardGroup) {
        super();
        this.boardGroup = boardGroup;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board_group, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_board_group);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TasksRecyclerAdapter(boardGroup.getTasks()));
        final Button button = view.findViewById(R.id.add_task);
        final EditText editText = view.findViewById(R.id.add_task_edit);
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
}
