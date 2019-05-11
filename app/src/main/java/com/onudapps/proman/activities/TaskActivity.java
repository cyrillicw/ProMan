package com.onudapps.proman.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.onudapps.proman.R;

public class TaskActivity extends AppCompatActivity {
    private enum DialogMode{
        MAIN, CALENDAR, TIME;
    }

    private DialogMode mode;
    private TimePicker timePicker;
    private RelativeLayout relativeLayout;
    private TextView selectedDate;
    private TextView selectedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mode = DialogMode.MAIN;
        final TextView description = findViewById(R.id.detailed_task_description);
        final EditText descriptionEdit = findViewById(R.id.detailed_task_description_edit);
        description.setOnClickListener(v -> {
            description.setVisibility(View.INVISIBLE);
            descriptionEdit.setVisibility(View.VISIBLE);
        });
        TextView textView = findViewById(R.id.detailed_task_start_text);
        textView.setOnClickListener(v -> {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(R.layout.alert_date);
            alertDialogBuilder.setPositiveButton(R.string.ok, null);
            alertDialogBuilder.setNegativeButton(R.string.cancel, (d, w) -> {});
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(b -> {
                switch (mode) {
                    case TIME:
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        selectedTime.setText(hour + ":" + minute);
                        timePicker.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        break;
                }
            });
            selectedDate = alertDialog.findViewById(R.id.selected_date);
            relativeLayout = alertDialog.findViewById(R.id.datetime_layout);
            selectedTime = alertDialog.findViewById(R.id.selected_time);
            CalendarView calendarView = alertDialog.findViewById(R.id.calendar);
            timePicker = alertDialog.findViewById(R.id.time);
            timePicker.setIs24HourView(DateFormat.is24HourFormat(v.getContext()));
            // TextView date = alertDialog.findViewById(R.id.);
            selectedTime.setOnClickListener(view -> {
                mode = DialogMode.TIME;
                relativeLayout.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            });
        });
    }
}
