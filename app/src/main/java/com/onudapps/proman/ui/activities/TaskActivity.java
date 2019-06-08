package com.onudapps.proman.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.GroupShortInfo;
import com.onudapps.proman.data.pojo.Task;
import com.onudapps.proman.ui.dialog_fragments.DateDialogFragment;
import com.onudapps.proman.ui.dialog_fragments.TaskChangeGroupDialogFragment;
import com.onudapps.proman.ui.dialog_fragments.TaskParticipantsDialogFragment;
import com.onudapps.proman.ui.listeners.DateDialogListener;
import com.onudapps.proman.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.FINISH_DIALOG_REQUEST_CODE;
import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.START_DIALOG_REQUEST_CODE;
import static com.onudapps.proman.viewmodels.TaskViewModel.EditMode.*;

public class TaskActivity extends AppCompatActivity implements DateDialogListener {
    public static final String TASK_ID_TAG = "taskId";
    public static final String BOARD_ID_TAG = "boardId";

    private ImageView tick;
    private ImageView cross;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private TextView group;
    private TextView dateStartText;
    private TextView dateFinishText;
    private RelativeLayout editToolbarLayout;
    private RelativeLayout defaultToolbarLayout;

    private TaskDBEntity task;
    private int taskId;
    private int boardId;
    private TaskViewModel taskViewModel;
    private List<GroupShortInfo> groups;
    private InputMethodManager imm;
    private boolean gotTaskData;
    private boolean gotGroupsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Intent intent = getIntent();
        boardId = intent.getIntExtra(BOARD_ID_TAG, -1);
        taskId = intent.getIntExtra(TASK_ID_TAG, -1);
        gotTaskData = false;
        gotGroupsData = false;
        taskViewModel = ViewModelProviders
                .of(this, new TaskViewModel.TaskModelFactory(taskId, boardId))
                .get(TaskViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView update = toolbar.findViewById(R.id.update);
        update.setOnClickListener(this::updateOnClickListener);
        tick = findViewById(R.id.tick);
        cross = findViewById(R.id.cross);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        defaultToolbarLayout = findViewById(R.id.default_toolbar_layout);
        editToolbarLayout = findViewById(R.id.edit_toolbar_layout);
        tick.setOnClickListener(this::tickOnClickListener);
        cross.setOnClickListener(this::crossOnClickListener);
        titleEdit = findViewById(R.id.detailed_task_title_edit);
        titleEdit.setOnTouchListener(this::titleOnClickListener);
        disableEditText(titleEdit);
        descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        descriptionEdit.setOnTouchListener(this::descriptionOnClickListener);
        disableEditText(descriptionEdit);
        group = findViewById(R.id.detailed_task_group);
        dateStartText = findViewById(R.id.detailed_task_start_text);
        dateFinishText = findViewById(R.id.detailed_task_finish_text);
        TextView participantsText = findViewById(R.id.detailed_task_participants_text);
        participantsText.setOnClickListener(this::participantsOnClickListener);
        dateStartText.setOnClickListener(this::startOnClickListener);
        dateFinishText.setOnClickListener(this::finishOnClickListener);
        LiveData<Task> taskData = taskViewModel.getTaskData();
        LiveData<List<GroupShortInfo>> groupsData = taskViewModel.getGroupsData();
        taskData.observe(this, this::taskDataListener);
        groupsData.observe(this, this::groupsDataListener);
        group.setOnClickListener(this::groupOnClickListener);
    }

    private void groupsDataListener(List<GroupShortInfo> groups) {
        this.groups = groups;
        gotGroupsData = true;
    }

    private void updateOnClickListener(View v) {
        taskViewModel.forceTaskUpdate();
    }

    private void groupOnClickListener(View v) {
        if (gotTaskData && gotGroupsData) {
            List<String> groupsTitles = new ArrayList<>();
            List<Integer> groupsIds = new ArrayList<>();
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).getGroupId() != task.getGroupId()) {
                    groupsTitles.add(groups.get(i).getGroupTitle());
                    groupsIds.add(groups.get(i).getGroupId());
                }
            }
            int[] groupsIdsArray = new int[groupsIds.size()];
            for (int i = 0; i < groupsIds.size(); i++) {
                groupsIdsArray[i] = groupsIds.get(i);
            }
            TaskChangeGroupDialogFragment.newInstance(groupsTitles.toArray(new String[0]), groupsIdsArray, taskId)
                    .show(getSupportFragmentManager(), null);
        }
    }

    private void taskDataListener(Task t) {
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.HOUR_OF_DAY, -1);
        if (t != null) {
            gotTaskData = true;
            task = t.getTaskDBEntity();
            refreshTitle();
            refreshDescription();
            group.setText(t.getGroupTitle());
        }
        if (t == null || t.getUpdated() == null || t.getUpdated().before(threshold)) {
            taskViewModel.forceTaskUpdate();
        }
    }

    private void startOnClickListener(View v) {
        if (gotTaskData) {
            DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(START_DIALOG_REQUEST_CODE, task.getStart());
            dateDialogFragment.show(getSupportFragmentManager(), "DATE START");
        }
    }

    private void finishOnClickListener(View v) {
        if (gotTaskData) {
            DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(FINISH_DIALOG_REQUEST_CODE, task.getFinish());
            dateDialogFragment.show(getSupportFragmentManager(), "DATE FINISH");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    @Override
    public void onDateSet(int requestCode, Calendar calendar) {
        if (requestCode == START_DIALOG_REQUEST_CODE) {
            taskViewModel.updateStart(calendar);
        }
        else {
            taskViewModel.updateFinish(calendar);
        }
        Toast.makeText(this, R.string.update_alert, Toast.LENGTH_LONG).show();
    }

    private void tickOnClickListener(View v) {
        TaskViewModel.EditMode editMode = taskViewModel.getEditMode();
        String s;
        switch (editMode) {
            case DESCRIPTION:
                s = descriptionEdit.getText().toString();
                taskViewModel.setEditMode(DEFAULT);
                refreshDescription();
                imm.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
                taskViewModel.updateDescription(s);
                break;
            case TITLE:
                s = titleEdit.getText().toString();
                if (s.length() == 0) {
                    return;
                }
                taskViewModel.setEditMode(DEFAULT);
                imm.hideSoftInputFromWindow(titleEdit.getWindowToken(), 0);
                refreshTitle();
                taskViewModel.updateTitle(s);
                break;
        }
        defaultToolbarLayout.setVisibility(View.VISIBLE);
        editToolbarLayout.setVisibility(View.INVISIBLE);
        taskViewModel.setEditMode(DEFAULT);
        Toast.makeText(this, R.string.update_alert, Toast.LENGTH_LONG).show();
    }

    private void crossOnClickListener(View v) {
        TaskViewModel.EditMode editMode = taskViewModel.getEditMode();
        defaultToolbarLayout.setVisibility(View.VISIBLE);
        editToolbarLayout.setVisibility(View.INVISIBLE);
        switch (editMode) {
            case DESCRIPTION:
                taskViewModel.setEditMode(DEFAULT);
                refreshDescription();
                imm.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
                break;
            case TITLE:
                taskViewModel.setEditMode(DEFAULT);
                refreshTitle();
                imm.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
                break;
        }
        taskViewModel.setEditMode(DEFAULT);
    }

    private void disableEditText(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setSingleLine(false);
        editText.setCursorVisible(false);
                DrawableCompat.setTint(editText.getBackground(),
                ContextCompat.getColor(this, R.color.transparent));
    }

    private void enableEditText(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setCursorVisible(true);
        editText.setSingleLine(false);
        DrawableCompat.setTint(editText.getBackground(),
                ContextCompat.getColor(this, R.color.colorAccent));
        editText.setSelection(editText.getText().length());
    }

    private void enableEditMode(EditText editText) {
        editToolbarLayout.setVisibility(View.VISIBLE);
        defaultToolbarLayout.setVisibility(View.INVISIBLE);
        enableEditText(editText);
    }

    private void participantsOnClickListener(View v) {
        if (gotTaskData) {
            TaskParticipantsDialogFragment.newInstance(taskId).show(getSupportFragmentManager(), null);
        }
    }

    private boolean descriptionOnClickListener(View v, MotionEvent motionEvent) {
        if (taskViewModel.getEditMode() == DEFAULT && gotTaskData) {
            taskViewModel.setEditMode(DESCRIPTION);
            enableEditMode(descriptionEdit);
            descriptionEdit.requestFocus();
            imm.showSoftInput(descriptionEdit, InputMethodManager.SHOW_IMPLICIT);
        }
        return true;
    }

    private boolean titleOnClickListener(View v, MotionEvent motionEvent) {
        if (taskViewModel.getEditMode() == DEFAULT && gotTaskData) {
            taskViewModel.setEditMode(TITLE);
            enableEditMode(titleEdit);
            titleEdit.requestFocus();
            imm.showSoftInput(titleEdit, InputMethodManager.SHOW_IMPLICIT);
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        TaskViewModel.EditMode editMode = taskViewModel.getEditMode();
        switch (editMode) {
            case DESCRIPTION:
                enableEditMode(descriptionEdit);
                descriptionEdit.requestFocus();
                imm.showSoftInput(descriptionEdit, InputMethodManager.SHOW_IMPLICIT);
                break;
            case TITLE:
                enableEditMode(titleEdit);
                descriptionEdit.requestFocus();
                imm.showSoftInput(descriptionEdit, InputMethodManager.SHOW_IMPLICIT);
                break;
        }
        super.onStart();
    }

    private void refreshDescription() {
        if (taskViewModel.getEditMode() != DESCRIPTION) {
            String s = task.getDescription();
            if (s == null || s.length() == 0) {
                descriptionEdit.setHint(R.string.change_description);
            } else {
                descriptionEdit.setText(task.getDescription());
            }
            disableEditText(descriptionEdit);
        }
    }

    private void refreshTitle() {
        if (taskViewModel.getEditMode() != TITLE) {
            titleEdit.setText(task.getTitle());
            disableEditText(titleEdit);
        }
    }
}
