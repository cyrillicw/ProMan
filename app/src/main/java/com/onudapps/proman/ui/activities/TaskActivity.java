package com.onudapps.proman.ui.activities;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.onudapps.TaskChange;
import com.onudapps.proman.BuildConfig;
import com.onudapps.proman.R;
import com.onudapps.proman.data.ProManDao;
import com.onudapps.proman.data.ProManDatabase;
import com.onudapps.proman.data.db.entities.BoardDBEntity;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;
import com.onudapps.proman.data.pojo.Task;
import com.onudapps.proman.ui.adapters.ParticipantsAdapter;
import com.onudapps.proman.viewmodels.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskActivity extends AppCompatActivity {

    private static final String LOG_TAG = "TaskActivity";
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private enum DialogMode {
        MAIN, CALENDAR, TIME;
    }
    private enum CalendarMode {
        START, FINISH;
    }
    private TextView title;

    private Task task;
    private Task editedTask;
    private ImageView tick;
    private ImageView upload;
    private TextView description;
    private EditText descriptionEdit;
    private TaskChange taskChange;
    private UUID taskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        // insert();
        taskId = UUID.fromString("8537dee2-2b9f-4658-b500-71d582628f6e");
        TaskViewModel taskViewModel = ViewModelProviders
                .of(this, new TaskViewModel.TaskModelFactory(getApplication(), taskId))
                .get(TaskViewModel.class);
        LiveData<Task> data = taskViewModel.getData();
        tick = findViewById(R.id.tick);
        upload = findViewById(R.id.upload);
        description = findViewById(R.id.detailed_task_description);
        descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        description.setOnClickListener(this::descriptionClickListener);
        tick.setOnClickListener(this::tickClickListener);
        upload.setOnClickListener(this::uploadClickListener);
        title = findViewById(R.id.detailed_task_title);
        TextView dateStartText = findViewById(R.id.detailed_task_start_text);
        TextView dateFinishText = findViewById(R.id.detailed_task_finish_text);
        TextView participantsText = findViewById(R.id.detailed_task_participants_text);
        participantsText.setOnClickListener(this::participantsOnClickListener);
        data.observe(this, t -> {
             task = t;
             editedTask = new Task(task);
             taskChange = new TaskChange(task);
             editedTask.setTaskChange(taskChange);
             title.setText(task.getTitle());
             refreshDescription();
             dateStartText.setOnClickListener(new DateDialog(editedTask, CalendarMode.START));
             dateFinishText.setOnClickListener(new DateDialog(editedTask, CalendarMode.FINISH));
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void tickClickListener(View v) {
        tick.setVisibility(View.INVISIBLE);
        Log.e("TICK", "HERE");
        editedTask.setDescription(descriptionEdit.getText().toString());
        checkChanges();
        refreshDescription();
        descriptionEdit.setVisibility(View.GONE);
        description.setVisibility(View.VISIBLE);
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
    }

    private void participantsOnClickListener(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.participants).create();
        alertDialog.show();
        RecyclerView recyclerView = alertDialog.findViewById(R.id.recycler_participants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ParticipantsAdapter(editedTask.getParticipants()));
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
//                participantDBEntity.setPublicKey("cyrillicw " + i);
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

    private void descriptionClickListener(View v) {
        description.setVisibility(View.INVISIBLE);
        tick.setVisibility(View.VISIBLE);
        if (editedTask.getDescription() == null || editedTask.getDescription().length() == 0) {
            descriptionEdit.setHint(getResources().getString(R.string.change_description));
        }
        else {
            descriptionEdit.setText(editedTask.getDescription());
        }
//        descriptionEdit.requestFocus();
//        InputMethodManager inputMethodManager =
//                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(descriptionEdit, InputMethodManager.SHOW_FORCED);
        descriptionEdit.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(descriptionEdit, 0);
    }

    private void refreshDescription() {
        if (editedTask.getDescription() == null || editedTask.getDescription().length() == 0) {
            description.setText(getResources().getString(R.string.change_description));
            description.setTypeface(null, Typeface.ITALIC);
        }
        else {
            description.setText(editedTask.getDescription());
            description.setTypeface(null, Typeface.NORMAL);
        }
    }

    private class DateDialog implements View.OnClickListener {

        private Task task;
        private Calendar calendar;
        private Calendar editCalendar;
        private CalendarMode calendarMode;
        private DialogMode dialogMode;
        private TimePicker timePicker;
        private DatePicker datePicker;
        private RelativeLayout relativeLayout;
        private TextView selectedDate;
        private TextView selectedTime;
        private DateDialog(Task task, CalendarMode calendarMode) {
            this.task = task;
            this.calendarMode = calendarMode;
            if (calendarMode == CalendarMode.START) {
                calendar = task.getStart();
            }
            else {
                calendar = task.getFinish();
            }
            dialogMode = DialogMode.MAIN;
        }

        @Override
        public void onClick(View v) {
            editCalendar = Calendar.getInstance();
            editCalendar.setTimeInMillis(calendar.getTimeInMillis());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskActivity.this);
            alertDialogBuilder.setView(R.layout.alert_date);
            alertDialogBuilder.setPositiveButton(R.string.ok, null);
            alertDialogBuilder.setNegativeButton(R.string.cancel, null);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            selectedDate = alertDialog.findViewById(R.id.selected_date);
            relativeLayout = alertDialog.findViewById(R.id.datetime_layout);
            selectedTime = alertDialog.findViewById(R.id.selected_time);
            datePicker = alertDialog.findViewById(R.id.calendar);
            timePicker = alertDialog.findViewById(R.id.time);
            timePicker.setIs24HourView(DateFormat.is24HourFormat(TaskActivity.this));
            selectedDate.setText(dateFormat.format(calendar.getTime()));
            selectedTime.setText(timeFormat.format(calendar.getTime()));
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(b -> {
                switch (dialogMode) {
                    case TIME:
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        editCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        editCalendar.set(Calendar.MINUTE, minute);
                        selectedTime.setText(timeFormat.format(editCalendar.getTime()));
                        timePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        dialogMode = DialogMode.MAIN;
                        break;
                    case CALENDAR:
                        editCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        editCalendar.set(Calendar.MONTH, datePicker.getMonth());
                        editCalendar.set(Calendar.YEAR, datePicker.getYear());
                        selectedDate.setText(dateFormat.format(editCalendar.getTimeInMillis()));
                        datePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        dialogMode = DialogMode.MAIN;
                        break;
                    default:
                        if (calendarMode == CalendarMode.START) {
                            task.setStart(editCalendar);
                        }
                        else {
                            task.setFinish(editCalendar);
                        }
                        checkChanges();
                        alertDialog.dismiss();
                }
            });
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(view -> {
                switch (dialogMode) {
                    case TIME:
                        timePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        dialogMode = DialogMode.MAIN;
                        break;
                    case CALENDAR:
                        datePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        dialogMode = DialogMode.MAIN;
                        break;
                    default:
                         alertDialog.dismiss();
                }
            });
            // TextView date = alertDialog.findViewById(R.id.);
            selectedTime.setOnClickListener(view -> {
                dialogMode = DialogMode.TIME;
                relativeLayout.setVisibility(View.GONE);
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
                timePicker.setVisibility(View.VISIBLE);
            });
            selectedDate.setOnClickListener(view -> {
                dialogMode = DialogMode.CALENDAR;
                relativeLayout.setVisibility(View.GONE);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                // calendarView.setDate(calendar.getTimeInMillis());
                datePicker.setVisibility(View.VISIBLE);
            });
        }
    }

    private void checkChanges() {
        if (taskChange.changesDetected()) {
            upload.setVisibility(View.VISIBLE);
        }
        else {
            upload.setVisibility(View.INVISIBLE);
        }
    }
}
