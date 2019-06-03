package com.onudapps.proman.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.data.db.entities.TaskDBEntity;
import com.onudapps.proman.data.pojo.GroupShortInfo;
import com.onudapps.proman.data.pojo.Task;
import com.onudapps.proman.ui.dialog_fragments.DateDialogFragment;
import com.onudapps.proman.ui.dialog_fragments.TaskChangeGroupDialogFragment;
import com.onudapps.proman.ui.listeners.DateDialogListener;
import com.onudapps.proman.viewmodels.TaskViewModel;

import java.util.Calendar;
import java.util.List;

import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.FINISH_DIALOG_REQUEST_CODE;
import static com.onudapps.proman.ui.dialog_fragments.DateDialogFragment.START_DIALOG_REQUEST_CODE;

public class TaskActivity extends AppCompatActivity implements DateDialogListener {

    private static final String LOG_TAG = "TaskActivity";
    public static final String TASK_ID_TAG = "taskId";
    public static final String BOARD_ID_TAG = "boardId";
    private enum CalendarMode {
        START, FINISH;
    }
    private TextView title;

    //private Task task;
    private ImageView tick;
    private ImageView upload;
    private TextView description;
    private EditText descriptionEdit;
    private TextView group;
    private TextView dateStartText;
    private TextView dateFinishText;

    private EditMode editMode;
    private TaskDBEntity task;
    private int taskId;
    private int boardId;
    private TaskViewModel taskViewModel;
    private String[] groupsTitles;
    private int[] groupsIds;

    private enum EditMode {
        TITLE, DESCRIPTION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        // insert();
        editMode = null;
        Intent intent = getIntent();
        boardId = intent.getIntExtra(BOARD_ID_TAG, -1);
        taskId = intent.getIntExtra(TASK_ID_TAG, -1);
        taskViewModel = ViewModelProviders
                .of(this, new TaskViewModel.TaskModelFactory(taskId, boardId))
                .get(TaskViewModel.class);
        tick = findViewById(R.id.tick);
        upload = findViewById(R.id.upload);
        description = findViewById(R.id.detailed_task_description);
        descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        //description.setOnClickListener(this::descriptionClickListener);
        tick.setOnClickListener(this::tickClickListener);
        upload.setOnClickListener(this::uploadClickListener);
        title = findViewById(R.id.detailed_task_title);
        group = findViewById(R.id.detailed_task_group);
        dateStartText = findViewById(R.id.detailed_task_start_text);
        dateFinishText = findViewById(R.id.detailed_task_finish_text);
        TextView participantsText = findViewById(R.id.detailed_task_participants_text);
        participantsText.setOnClickListener(this::participantsOnClickListener);
        LiveData<Task> taskData = taskViewModel.getTaskData();
        LiveData<List<GroupShortInfo>> groupsData = taskViewModel.getGroupsData();
        taskData.observe(this, this::taskDataListener);
        groupsData.observe(this, this::groupsDataListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void groupsDataListener(List<GroupShortInfo> groups) {
        groupsTitles = new String[groups.size()];
        groupsIds = new int[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            groupsTitles[i] = groups.get(i).getGroupTitle();
            groupsIds[i] = groups.get(i).getGroupId();
        }
        group.setOnClickListener(this::groupOnClickListener);
    }

    private void groupOnClickListener(View v) {
        TaskChangeGroupDialogFragment.newInstance(groupsTitles, groupsIds, taskId)
                .show(getSupportFragmentManager(), null);
    }

    private void taskDataListener(Task t) {
        task = t.getTaskDBEntity();
//            editedTask = new Task(task);
//            taskChange = new TaskChange(task);
//            editedTask.setTaskChange(taskChange);
        TaskDBEntity taskDBEntity = t.getTaskDBEntity();
        title.setText(task.getTitle());
        refreshDescription();
        description.setOnClickListener(this::descriptionOnClickListener);
//            dateStartText.setOnClickListener(this::startOnClickListener);
//            dateFinishText.setOnClickListener(this::finishOnClickListener);
        dateStartText.setOnClickListener(this::startOnClickListener);
        dateFinishText.setOnClickListener(this::finishOnClickListener);
        group.setText(t.getGroupTitle());
    }

    private void startOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(START_DIALOG_REQUEST_CODE, task.getStart());
        dateDialogFragment.show(getSupportFragmentManager(), "DATE START");
    }

    private void finishOnClickListener(View v) {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(FINISH_DIALOG_REQUEST_CODE, task.getFinish());
        dateDialogFragment.show(getSupportFragmentManager(), "DATE FINISH");
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

    private void tickClickListener(View v) {
        tick.setVisibility(View.INVISIBLE);
        switch (editMode) {
            case DESCRIPTION:
                descriptionEdit.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
                taskViewModel.updateDescription(descriptionEdit.getText().toString());
                title.setOnClickListener(this::titleOnClickListener);
                Toast.makeText(this, R.string.update_alert, Toast.LENGTH_LONG).show();
        }
//        Log.e("TICK", "HERE");
//        editedTask.setDescription(descriptionEdit.getText().toString());
////        checkChanges();
//        refreshDescription();
//        descriptionEdit.setVisibility(View.GONE);
//        description.setVisibility(View.VISIBLE);
//        InputMethodManager inputMethodManager =
//                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
    }

    private void titleOnClickListener(View v) {}

    private void participantsOnClickListener(View v) {
//        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.participants).create();
//        alertDialog.show();
//        RecyclerView recyclerView = alertDialog.findViewById(R.id.recycler_participants);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(new ParticipantsAdapter(editedTask.getParticipants()));
    }

    private void uploadClickListener(View v) {

    }



//    private void  insert() {
//        ProManDatabase database = Room.databaseBuilder(this, ProManDatabase.class, "ProManDatabase").build();
//        ProManDao proManDao = database.getProManDao();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            BoardDBEntity boardDBEntity = new BoardDBEntity();
//            boardDBEntity.setBoardId(UUID.randomUUID());
//            boardDBEntity.setTitle("BOARD TEST 1");
//            Calendar start = Calendar.getInstance();
//            start.add(Calendar.DAY_OF_MONTH, -5);
//            Calendar finish = Calendar.getInstance();
//            finish.add(Calendar.DAY_OF_MONTH, 5);
//            boardDBEntity.setStart(start);
//            boardDBEntity.setFinish(finish);
//            GroupDBEntity groupDBEntity = new GroupDBEntity();
//            groupDBEntity.setGroupId(UUID.randomUUID());
//            groupDBEntity.setBoardId(boardDBEntity.getBoardId());
//            groupDBEntity.setTitle("GROUP TEST 1");
//            Task task = new Task();
//            List<ParticipantDBEntity> participantDBEntities = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                ParticipantDBEntity participantDBEntity = new ParticipantDBEntity();
//                participantDBEntity.setNickName("cyrillicw " + i);
//                participantDBEntity.setAddress("cyrillicw " + i);
//                participantDBEntities.add(participantDBEntity);
//            }
//            task.setTaskId(UUID.randomUUID());
//            task.setParticipants(participantDBEntities);
//            task.setTitle("HELLO");
//            task.setDescription("");
//            task.setStart(start);
//            task.setFinish(finish);
//            task.setBoardId(boardDBEntity.getBoardId());
//            task.setGroupId(groupDBEntity.getGroupId());
//            proManDao.insertBoard(boardDBEntity);
//            proManDao.insertGroup(groupDBEntity);
//            proManDao.insertTask(task);
//            Log.e("INSERTED", "YES");
//        });
//    }

    private void descriptionOnClickListener(View v) {
        editMode = EditMode.DESCRIPTION;
        title.setOnClickListener(null);
        description.setVisibility(View.GONE);
        tick.setVisibility(View.VISIBLE);
        if (task.getDescription() == null || task.getDescription().length() == 0) {
            descriptionEdit.setHint(getResources().getString(R.string.change_description));
        }
        else {
            descriptionEdit.setText(task.getDescription());
        }
//        descriptionEdit.requestFocus();
//        InputMethodManager inputMethodManager =
//                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(descriptionEdit, InputMethodManager.SHOW_FORCED);
        descriptionEdit.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(descriptionEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private void refreshDescription() {
        if (task.getDescription() == null || task.getDescription().length() == 0) {
            description.setText(getResources().getString(R.string.change_description));
            description.setTypeface(null, Typeface.ITALIC);
        }
        else {
            description.setText(task.getDescription());
            description.setTypeface(null, Typeface.NORMAL);
        }
    }

    //    private class DateDialog implements View.OnClickListener {
//
//        private Task task;
//        private Calendar calendar;
//        private Calendar editCalendar;
//        private CalendarMode calendarMode;
//        private DialogMode dialogMode;
//        private TimePicker timePicker;
//        private DatePicker datePicker;
//        private RelativeLayout relativeLayout;
//        private TextView selectedDate;
//        private TextView selectedTime;
//        private DateDialog(Task task, CalendarMode calendarMode) {
//            this.task = task;
//            this.calendarMode = calendarMode;
//            if (calendarMode == CalendarMode.START) {
//                calendar = task.getStart();
//            }
//            else {
//                calendar = task.getFinish();
//            }
//            dialogMode = DialogMode.MAIN;
//        }
//
//        @Override
//        public void onClick(View v) {
//            editCalendar = Calendar.getInstance();
//            editCalendar.setTimeInMillis(calendar.getTimeInMillis());
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskActivity.this);
//            alertDialogBuilder.setView(R.layout.alert_date);
//            alertDialogBuilder.setPositiveButton(R.string.ok, null);
//            alertDialogBuilder.setNegativeButton(R.string.cancel, null);
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//            selectedDate = alertDialog.findViewById(R.id.selected_date);
//            relativeLayout = alertDialog.findViewById(R.id.datetime_layout);
//            selectedTime = alertDialog.findViewById(R.id.selected_time);
//            datePicker = alertDialog.findViewById(R.id.calendar);
//            timePicker = alertDialog.findViewById(R.id.time);
//            timePicker.setIs24HourView(DateFormat.is24HourFormat(TaskActivity.this));
//            selectedDate.setText(dateFormat.format(calendar.getTime()));
//            selectedTime.setText(timeFormat.format(calendar.getTime()));
//            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//            positiveButton.setOnClickListener(b -> {
//                switch (dialogMode) {
//                    case TIME:
//                        int hour = timePicker.getHour();
//                        int minute = timePicker.getMinute();
//                        editCalendar.set(Calendar.HOUR_OF_DAY, hour);
//                        editCalendar.set(Calendar.MINUTE, minute);
//                        selectedTime.setText(timeFormat.format(editCalendar.getTime()));
//                        timePicker.setVisibility(View.GONE);
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        dialogMode = DialogMode.MAIN;
//                        break;
//                    case CALENDAR:
//                        editCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
//                        editCalendar.set(Calendar.MONTH, datePicker.getMonth());
//                        editCalendar.set(Calendar.YEAR, datePicker.getYear());
//                        selectedDate.setText(dateFormat.format(editCalendar.getTimeInMillis()));
//                        datePicker.setVisibility(View.GONE);
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        dialogMode = DialogMode.MAIN;
//                        break;
//                    default:
//                        if (calendarMode == CalendarMode.START) {
//                            task.setStart(editCalendar);
//                        }
//                        else {
//                            task.setFinish(editCalendar);
//                        }
//                        checkChanges();
//                        alertDialog.dismiss();
//                }
//            });
//            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//            negativeButton.setOnClickListener(view -> {
//                switch (dialogMode) {
//                    case TIME:
//                        timePicker.setVisibility(View.GONE);
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        dialogMode = DialogMode.MAIN;
//                        break;
//                    case CALENDAR:
//                        datePicker.setVisibility(View.GONE);
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        dialogMode = DialogMode.MAIN;
//                        break;
//                    default:
//                         alertDialog.dismiss();
//                }
//            });
//            // TextView date = alertDialog.findViewById(R.id.);
//            selectedTime.setOnClickListener(view -> {
//                dialogMode = DialogMode.TIME;
//                relativeLayout.setVisibility(View.GONE);
//                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
//                timePicker.setMinute(calendar.get(Calendar.MINUTE));
//                timePicker.setVisibility(View.VISIBLE);
//            });
//            selectedDate.setOnClickListener(view -> {
//                dialogMode = DialogMode.CALENDAR;
//                relativeLayout.setVisibility(View.GONE);
//                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//                // calendarView.setDate(calendar.getTimeInMillis());
//                datePicker.setVisibility(View.VISIBLE);
//            });
//        }
//    }

//    private void checkChanges() {
//        if (taskChange.changesDetected()) {
//            upload.setVisibility(View.VISIBLE);
//        }
//        else {
//            upload.setVisibility(View.INVISIBLE);
//        }
//    }
}
