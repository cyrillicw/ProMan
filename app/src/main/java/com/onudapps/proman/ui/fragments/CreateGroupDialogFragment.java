package com.onudapps.proman.ui.fragments;

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
import com.onudapps.proman.ui.activities.BoardActivity;

public class CreateGroupDialogFragment extends DialogFragment {
    private static final String LOG_TAG = "CreateGroupDialogFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_create, container);
        //viewModel = ViewModelProviders.of(this).get(CreateGroupViewModel.class);
        TextView textView = view.findViewById(R.id.create_hint);
        textView.setText(R.string.create_group);
        EditText title = view.findViewById(R.id.created_title);
        TextView ok = view.findViewById(R.id.confirm_button);
        ok.setOnClickListener(v -> {
            ((BoardActivity)getActivity()).getViewModel().createGroup(title.getText().toString());
            dismiss();
        });
        return view;
    }
}