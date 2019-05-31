package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.data.pojo.StartFinishDates;
import com.onudapps.proman.ui.activities.StartFinishViewSupport;
import com.onudapps.proman.ui.dialog_fragments.DateDialogFragment;
import com.onudapps.proman.ui.dialog_fragments.ParticipantsDialogFragment;
import com.onudapps.proman.viewmodels.BoardPropertiesViewModel;
import com.onudapps.proman.viewmodels.StartFinishViewModelSupport;

import java.util.List;

public class BoardPropertiesFragment extends Fragment implements StartFinishViewSupport {
    private static final String BOARD_ID_TAG = "boardId";

    private int boardId;
    private BoardPropertiesViewModel viewModel;

    public static BoardPropertiesFragment newInstance(int boardId) {
        Bundle args = new Bundle();
        args.putInt(BOARD_ID_TAG, boardId);
        BoardPropertiesFragment f = new BoardPropertiesFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardId = getArguments().getInt(BOARD_ID_TAG);
        View view = inflater.inflate(R.layout.fragment_board_properties, container, false);
        TextView dateStartText = view.findViewById(R.id.board_start_text);
        TextView dateFinishText = view.findViewById(R.id.board_finish_text);
        TextView participantsText = view.findViewById(R.id.board_participants_text);
        viewModel = ViewModelProviders
                .of(this, new BoardPropertiesViewModel.BoardPropertiesModelFactory(boardId))
                .get(BoardPropertiesViewModel.class);
        LiveData<List<ParticipantDBEntity>> participantsData = viewModel.getParticipantsData();
        LiveData<StartFinishDates> startFinishDatesData = viewModel.getDatesData();
        participantsData.observe(this, this::onParticipantsDataChangedListener);
        startFinishDatesData.observe(this, this::onDateDataChangedListener);
        dateStartText.setOnClickListener(this::startOnClickListener);
        dateFinishText.setOnClickListener(this::finishOnClickListener);
        participantsText.setOnClickListener(this::participantsOnClickListener);
        return view;
    }

    private void startOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(DateDialogFragment.CalendarType.START);
        dateDialogFragment.setViewModel(viewModel);
        dateDialogFragment.show(getActivity().getSupportFragmentManager(), "DATE START");
    }

    private void finishOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(DateDialogFragment.CalendarType.FINISH);
        dateDialogFragment.setViewModel(viewModel);
        dateDialogFragment.show(getActivity().getSupportFragmentManager(), "DATE FINISH");
    }

    private void participantsOnClickListener(View v) {
        ParticipantsDialogFragment participantsDialogFragment = ParticipantsDialogFragment.newInstance(boardId);
        participantsDialogFragment.show(getActivity().getSupportFragmentManager(), "participants");
    }

    @Override
    public StartFinishViewModelSupport getStartFinishViewModel() {
        return viewModel;
    }

    private void onParticipantsDataChangedListener(List<ParticipantDBEntity> participantDBEntities) {

    }

    private void onDateDataChangedListener(StartFinishDates startFinishDates) {
        viewModel.setStartChanged(startFinishDates.getStart());
        viewModel.setFinishChanged(startFinishDates.getStart());
    }
}
