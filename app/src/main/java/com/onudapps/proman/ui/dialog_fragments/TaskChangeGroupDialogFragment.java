package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.viewmodels.TaskChangeGroupViewModel;

public class TaskChangeGroupDialogFragment extends DialogFragment {
    private static final String GROUPS_TITLES_TAG = "groupsTitles";
    private static final String GROUPS_IDS_TAG = "groupsIds";
    private static final String TASK_ID_TAG = "taskID";
    private TaskChangeGroupViewModel viewModel;

    public static TaskChangeGroupDialogFragment newInstance(String[] groupsTitles, int[] groupsIds, int taskId) {
        Bundle args = new Bundle();
        TaskChangeGroupDialogFragment fragment = new TaskChangeGroupDialogFragment();
        args.putStringArray(GROUPS_TITLES_TAG, groupsTitles);
        args.putIntArray(GROUPS_IDS_TAG, groupsIds);
        args.putInt(TASK_ID_TAG, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] groupsTitles = getArguments().getStringArray(GROUPS_TITLES_TAG);
        int[] groupsIds =  getArguments().getIntArray(GROUPS_IDS_TAG);
        int taskId = getArguments().getInt(TASK_ID_TAG);
        viewModel = ViewModelProviders
                .of(this, new TaskChangeGroupViewModel.TaskChangeGroupModelFactory(groupsTitles, groupsIds, taskId))
                .get(TaskChangeGroupViewModel.class);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.move_to_group_title))
                .setItems(viewModel.getGroupsTitles(),
                (d, w) -> {
                    viewModel.moveTaskToGroup(taskId, w);
                    Toast.makeText(getContext(), R.string.update_alert, Toast.LENGTH_LONG).show();
                });
        return builder.create();
    }
}
