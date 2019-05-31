package com.onudapps.proman.ui.dialog_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.activities.CreateDialogListener;

public class CreateDialogFragment extends DialogFragment {
    private static final String LOG_TAG = "CreateDialogFragment";
    public static final String RETURN_TAG = "createdString";
    private static final String TITLE_TEXT_TAG = "title";

    public static CreateDialogFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TITLE_TEXT_TAG, title);
        CreateDialogFragment fragment = new CreateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_create, container);
        //viewModel = ViewModelProviders.of(this).get(CreateGroupViewModel.class);
        TextView textView = view.findViewById(R.id.create_hint);
        textView.setText(getArguments().getString(TITLE_TEXT_TAG));
        EditText title = view.findViewById(R.id.created_title);
        TextView ok = view.findViewById(R.id.confirm_button);
        ok.setOnClickListener(v -> {
            //((BoardActivity)getActivity()).getViewModel().createGroup(title.getText().toString());
            if (getTargetFragment() != null) {
                Intent intent = new Intent();
                intent.putExtra(RETURN_TAG, title.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
            else {
                ((CreateDialogListener)getActivity()).onCreateCommit(title.getText().toString());
            }
            dismiss();
        });
        return view;
    }
}