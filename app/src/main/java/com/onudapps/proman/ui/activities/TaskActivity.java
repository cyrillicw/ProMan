package com.onudapps.proman.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.onudapps.proman.R;
import com.onudapps.proman.data.entities.Task;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private enum DialogMode{
        MAIN, CALENDAR, TIME;
    }
    private TextView title;

    private Task task;
    private Task editedTask;
    private ImageView tick;
    private TextView description;
    private EditText descriptionEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        task = getTask();
        editedTask = new Task(task);
        tick = findViewById(R.id.tick);
        description = findViewById(R.id.detailed_task_description);
        refreshDescription();
        descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        description.setOnClickListener(v -> {
            description.setVisibility(View.INVISIBLE);
            tick.setVisibility(View.VISIBLE);
            if (editedTask.getDescription() == null || editedTask.getDescription().length() == 0) {
                descriptionEdit.setHint(getResources().getString(R.string.change_description));
            }
            else {
                descriptionEdit.setText(editedTask.getDescription());
            }
            descriptionEdit.requestFocus();
            inputMethodManager.showSoftInput(descriptionEdit, InputMethodManager.SHOW_FORCED);
            descriptionEdit.setVisibility(View.VISIBLE);
        });
        tick.setOnClickListener(v -> {
            tick.setVisibility(View.GONE);
            editedTask.setDescription(descriptionEdit.getText().toString());
            refreshDescription();
            descriptionEdit.setVisibility(View.GONE);
            description.setVisibility(View.VISIBLE);
            inputMethodManager.hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
        });
        title = findViewById(R.id.detailed_task_title);
        title.setText(task.getTitle());

        TextView dateStartText = findViewById(R.id.detailed_task_start_text);
        dateStartText.setOnClickListener(new DateDialog(editedTask.getStart()));
        TextView dateFinishText = findViewById(R.id.detailed_task_finish_text);
        dateFinishText.setOnClickListener(new DateDialog(editedTask.getFinish()));
        TextView participantsText = findViewById(R.id.detailed_task_participants_text);
        participantsText.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setView(R.layout.participants).create();
//            RecyclerView recyclerView = alertDialog.findViewById(R.id.recycler_participants);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), RecyclerView.VERTICAL, false);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(new ParticipantsAdapter(editedTask.getParticipants()        ));
        });
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

    private Task getTask() {
        Task task = new Task();
        String[] participantsArray = {"cyrillicw", "kneshta", "alyonaboo"};
        List<String> participants = Arrays.asList(participantsArray);
        task.setParticipants(participants);
        task.setTitle("HELLO");
        task.setStart(Calendar.getInstance());
        task.setFinish(Calendar.getInstance());
        // task.setDescription("To build a new cool mobile app");
        task.setDescription(null);
        return task;
    }

    private class DateDialog implements View.OnClickListener {
        private Calendar calendar;
        private Calendar editCalendar;
        private DialogMode mode;
        private TimePicker timePicker;
        private DatePicker datePicker;
        private RelativeLayout relativeLayout;
        private TextView selectedDate;
        private TextView selectedTime;
        private DateDialog(Calendar calendar) {
            this.calendar = calendar;
            mode = DialogMode.MAIN;
        }

        @Override
        public void onClick(View v) {
            Calendar editCalendar = Calendar.getInstance();
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
                switch (mode) {
                    case TIME:
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        editCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        editCalendar.set(Calendar.MINUTE, minute);
                        selectedTime.setText(timeFormat.format(editCalendar.getTime()));
                        timePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        mode = DialogMode.MAIN;
                        break;
                    case CALENDAR:
                        editCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        editCalendar.set(Calendar.MONTH, datePicker.getMonth());
                        editCalendar.set(Calendar.YEAR, datePicker.getYear());
                        selectedDate.setText(dateFormat.format(editCalendar.getTimeInMillis()));
                        datePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        mode = DialogMode.MAIN;
                        break;
                    default:
                        calendar.setTimeInMillis(editCalendar.getTimeInMillis());
                        alertDialog.dismiss();
                }
            });
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(view -> {
                switch (mode) {
                    case TIME:
                        timePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        mode = DialogMode.MAIN;
                        break;
                    case CALENDAR:
                        datePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        mode = DialogMode.MAIN;
                        break;
                    default:
                         alertDialog.dismiss();
                }
            });
            // TextView date = alertDialog.findViewById(R.id.);
            selectedTime.setOnClickListener(view -> {
                mode = DialogMode.TIME;
                relativeLayout.setVisibility(View.GONE);
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
                timePicker.setVisibility(View.VISIBLE);
            });
            selectedDate.setOnClickListener(view -> {
                mode = DialogMode.CALENDAR;
                relativeLayout.setVisibility(View.GONE);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                // calendarView.setDate(calendar.getTimeInMillis());
                datePicker.setVisibility(View.VISIBLE);
            });
        }
    }
}
