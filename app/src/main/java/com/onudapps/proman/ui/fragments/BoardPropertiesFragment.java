package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.data.pojo.StartFinishDates;
import com.onudapps.proman.ui.dialog_fragments.BoardParticipantsDialogFragment;
import com.onudapps.proman.ui.dialog_fragments.DateDialogFragment;
import com.onudapps.proman.ui.listeners.DateDialogListener;
import com.onudapps.proman.viewmodels.BoardPropertiesViewModel;

import java.util.Calendar;

import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.FINISH_DIALOG_REQUEST_CODE;
import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.START_DIALOG_REQUEST_CODE;

public class BoardPropertiesFragment extends Fragment implements DateDialogListener {
    private static final String BOARD_ID_TAG = "boardId";
    private int boardId;
    private BoardPropertiesViewModel viewModel;
    private Calendar start;
    private Calendar finish;

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
        LiveData<StartFinishDates> startFinishDatesData = viewModel.getDatesData();
        startFinishDatesData.observe(this, this::onDateDataChangedListener);
        dateStartText.setOnClickListener(this::startOnClickListener);
        dateFinishText.setOnClickListener(this::finishOnClickListener);
        participantsText.setOnClickListener(this::participantsOnClickListener);
        return view;
    }

    private void startOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(START_DIALOG_REQUEST_CODE, start);
        dateDialogFragment.setTargetFragment(this, START_DIALOG_REQUEST_CODE);
        dateDialogFragment.show(getActivity().getSupportFragmentManager(), "DATE START");
    }

    private void finishOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(FINISH_DIALOG_REQUEST_CODE, finish);
        dateDialogFragment.setTargetFragment(this, FINISH_DIALOG_REQUEST_CODE);
        dateDialogFragment.show(getActivity().getSupportFragmentManager(), "DATE FINISH");
    }

    private void participantsOnClickListener(View v) {
        BoardParticipantsDialogFragment boardParticipantsDialogFragment = BoardParticipantsDialogFragment.newInstance(boardId);
        boardParticipantsDialogFragment.show(getActivity().getSupportFragmentManager(), "participants");
    }

    private void onDateDataChangedListener(StartFinishDates startFinishDates) {
        if (startFinishDates == null) {
            start = null;
            finish = null;
        }
        else {
            start = startFinishDates.getStart();
            finish = startFinishDates.getFinish();
        }
    }

    @Override
    public void onDateSet(int requestCode, Calendar calendar) {
        if (requestCode == START_DIALOG_REQUEST_CODE) {
            viewModel.updateStart(calendar);
        }
        else {
            viewModel.updateFinish(calendar);
        }
        Toast.makeText(getContext(), R.string.update_alert, Toast.LENGTH_LONG).show();
    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == START_DIALOG_REQUEST_CODE || requestCode == FINISH_DIALOG_REQUEST_CODE) {
//                long datetime = data.getLongExtra(DateDialogFragment.RETURN_TAG, -1);
//                Calendar calendar;
//                if (datetime == -1) {
//                    calendar = null;
//                } else {
//                    calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(datetime);
//                }
//                if (requestCode == START_DIALOG_REQUEST_CODE) {
//                    viewModel.updateStart(calendar);
//                }
//                else {
//                    viewModel.updateFinish(calendar);
//                }
//                Toast.makeText(getContext(), R.string.update_alert, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
