package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
                .setPositiveButton(R.string.ok, null).create();
        dialog.show();
        TextView textView = dialog.findViewById(R.id.create_hint);
        textView.setText(getArguments().getString(TITLE_TEXT_TAG));
        title = dialog.findViewById(R.id.created_title);
        title.setHint(getArguments().getString(HINT_TEXT_TAG));
        Button ok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(this::okOnClickListener);
        return dialog;
    }


    private void okOnClickListener(View v) {
        String res = title.getText().toString();
        if (!res.equals("")) {
            if (getTargetFragment() != null) {
                ((CreateDialogListener)getTargetFragment()).onCreateCommit(title.getText().toString());
            } else {
                ((CreateDialogListener) getActivity()).onCreateCommit(title.getText().toString());
            }
            Toast.makeText(getContext(), R.string.update_alert, Toast.LENGTH_LONG).show();
            dismiss();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}