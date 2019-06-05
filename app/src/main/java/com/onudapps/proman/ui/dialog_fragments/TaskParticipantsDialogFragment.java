package com.onudapps.proman.ui.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onudapps.proman.R;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.ui.adapters.ParticipantsAdapter;
import com.onudapps.proman.ui.listeners.CreateDialogListener;
import com.onudapps.proman.viewmodels.TaskParticipantsViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskParticipantsDialogFragment extends DialogFragment implements CreateDialogListener {
    private static final String TASK_ID_TAG = "taskId";
    private static final int DIALOG_REQUEST_CODE = 1;

    private int taskId;
    private TaskParticipantsViewModel viewModel;

    public static TaskParticipantsDialogFragment newInstance(int taskId) {
        Bundle args = new Bundle();
        args.putInt(TASK_ID_TAG, taskId);
        TaskParticipantsDialogFragment f = new TaskParticipantsDialogFragment();
        f.setArguments(args);
        return f;
    }

    private RecyclerView participantsRecycler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_participants, container, false);
        participantsRecycler = view.findViewById(R.id.recycler_participants);
        taskId = getArguments().getInt(TASK_ID_TAG);
        viewModel = ViewModelProviders
                .of(this, new TaskParticipantsViewModel.TaskParticipantsModelFactory(taskId))
                .get(TaskParticipantsViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        participantsRecycler.setLayoutManager(layoutManager);
        ParticipantsAdapter participantsAdapter = new ParticipantsAdapter(taskId, new ArrayList<>());
        participantsRecycler.setAdapter(participantsAdapter);
        LiveData<List<ParticipantDBEntity>> participantsData = viewModel.getParticipantsData();
        participantsData.observe(this, this::onParticipantsDataChanged);
        FloatingActionButton addParticipants = view.findViewById(R.id.add_participants_button);
        addParticipants.setOnClickListener(this::addParticipantsClickListener);
        return view;
    }

    private void onParticipantsDataChanged(List<ParticipantDBEntity> participants) {
        ((ParticipantsAdapter)participantsRecycler.getAdapter()).updateData(participants);
    }

    private void addParticipantsClickListener(View v) {
        CreateDialogFragment createDialogFragment = CreateDialogFragment.newInstance(getResources().getString(R.string.add_participant));
        createDialogFragment.setTargetFragment(this, DIALOG_REQUEST_CODE);
        createDialogFragment.show(getActivity().getSupportFragmentManager(), "Create participant");
    }

    @Override
    public void onCreateCommit(String res) {
        viewModel.addTaskParticipant(res.toLowerCase());
    }
}
