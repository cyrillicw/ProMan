package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.onudapps.proman.R;
import com.onudapps.proman.data.Repository;

public class BoardsMenuDialogFragment extends DialogFragment {
    private static final String BOARD_ID_TAG = "boardId";

    public static BoardsMenuDialogFragment newInstance(int boardId) {
        Bundle args = new Bundle();
        args.putInt(BOARD_ID_TAG, boardId);
        BoardsMenuDialogFragment f = new BoardsMenuDialogFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int boardId = getArguments().getInt(BOARD_ID_TAG);
        String[] options = {getResources().getString(R.string.leave_board)};
        return new AlertDialog.Builder(getContext()).setItems(options, (d, w) -> {
            if (w == 0) {
                Repository.REPOSITORY.leaveBoard(boardId);
            }
        }).create();
    }
}
