package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.listeners.CreateDialogListener;

public class CreateDialogFragment extends DialogFragment {
    private static final String TITLE_TEXT_TAG = "title";
    private static final String HINT_TEXT_TAG = "hint";

    private EditText title;

    public static CreateDialogFragment newInstance(String title, String hint) {
        Bundle args = new Bundle();
        args.putString(TITLE_TEXT_TAG, title);
        args.putString(HINT_TEXT_TAG, hint);
        CreateDialogFragment fragment = new CreateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(R.layout.alert_create)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        dialog.show();
        TextView textView = dialog.findViewById(R.id.create_hint);
        textView.setText(getArguments().getString(TITLE_TEXT_TAG));
        title = dialog.findViewById(R.id.created_title);
        title.setHint(getArguments().getString(HINT_TEXT_TAG));
        Button ok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button cancel = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        ok.setOnClickListener(this::okOnClickListener);
        cancel.setOnClickListener(this::cancelOnClickListener);
        return dialog;
    }

    private void okOnClickListener(View v) {
        String res = title.getText().toString().trim();
        if (!res.equals("")) {
            if (getTargetFragment() != null) {
                ((CreateDialogListener)getTargetFragment()).onCreateCommit(res);
            } else {
                ((CreateDialogListener) getActivity()).onCreateCommit(res);
            }
            Toast.makeText(getContext(), R.string.update_alert, Toast.LENGTH_LONG).show();
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            dismiss();
        }
    }

    private void cancelOnClickListener(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}