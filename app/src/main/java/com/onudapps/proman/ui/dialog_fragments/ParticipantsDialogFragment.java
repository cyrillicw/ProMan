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
import com.onudapps.proman.viewmodels.BoardParticipantsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsDialogFragment extends DialogFragment implements CreateDialogListener {
    private static final String BOARD_ID_TAG = "boardId";
    private static final int DIALOG_REQUEST_CODE = 1;

    private int boardId;
    private BoardParticipantsViewModel viewModel;

    public static ParticipantsDialogFragment newInstance(int boardId) {
        Bundle args = new Bundle();
        args.putInt(BOARD_ID_TAG, boardId);
        ParticipantsDialogFragment f = new ParticipantsDialogFragment();
        f.setArguments(args);
        return f;
    }

    private RecyclerView participantsRecycler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_participants, container, false);
        participantsRecycler = view.findViewById(R.id.recycler_participants);
        boardId = getArguments().getInt(BOARD_ID_TAG);
        viewModel = ViewModelProviders
                .of(this, new BoardParticipantsViewModel.BoardParticipantsModelFactory(boardId))
                .get(BoardParticipantsViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        participantsRecycler.setLayoutManager(layoutManager);
        ParticipantsAdapter participantsAdapter = new ParticipantsAdapter(boardId, new ArrayList<>());
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
        viewModel.addBoardParticipant(res);
    }
}
