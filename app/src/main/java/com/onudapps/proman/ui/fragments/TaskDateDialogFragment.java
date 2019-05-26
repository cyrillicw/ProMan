package com.onudapps.proman.ui.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.onudapps.proman.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDateDialogFragment extends DialogFragment {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private static final String dateTimeTag = "datetime";
    private static final String calendarTypeTag = "calendarType";

    private enum DialogMode {
        MAIN, CALENDAR, TIME;
    }

    private TimePicker timePicker;
    private DatePicker datePicker;
    private RelativeLayout relativeLayout;
    private TextView selectedDate;
    private TextView selectedTime;
    private Calendar originalCalendar;
    private Calendar editCalendar;
    private DialogMode dialogMode;
    private boolean dateSet;
    private boolean timeSet;

    public static TaskDateDialogFragment newInstance(long datetime) {
        Bundle args = new Bundle();
        args.putLong(dateTimeTag, datetime);
//        args.putString(calendarTypeTag, );
        TaskDateDialogFragment fragment = new TaskDateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_date, container);
        dialogMode = DialogMode.MAIN;
        long datetime = getArguments().getLong(dateTimeTag, -1);
        if (datetime != -1) {
            originalCalendar = Calendar.getInstance();
            originalCalendar.setTimeInMillis(datetime);
        }
        else {
            originalCalendar = null;
        }
        selectedDate = view.findViewById(R.id.selected_date);
        selectedTime = view.findViewById(R.id.selected_time);
        editCalendar = Calendar.getInstance();
        if (originalCalendar != null) {
            editCalendar.setTimeInMillis(originalCalendar.getTimeInMillis());
            selectedDate.setText(dateFormat.format(editCalendar.getTime()));
            selectedTime.setText(timeFormat.format(editCalendar.getTime()));
            dateSet = true;
            timeSet = true;
        }
        else {
            selectedDate.setText("DATE IS NOT SET");
            dateSet = false;
            timeSet = false;
        }
        datePicker = view.findViewById(R.id.calendar);
        timePicker = view.findViewById(R.id.time);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        Button positiveButton = view.findViewById(R.id.confirm_button);
        Button negativeButton = view.findViewById(R.id.cancel_button);
        positiveButton.setOnClickListener(this::positiveButtonOnClickListener);
        negativeButton.setOnClickListener(this::negativeButtonOnClickListener);
        selectedTime.setOnClickListener(this::selectedTimeOnClickListener);
//        selectedDate.setOnClickListener(view -> {
//            dialogMode = TaskActivity.DialogMode.CALENDAR;
//            relativeLayout.setVisibility(View.GONE);
//            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//            // calendarView.setDate(calendar.getTimeInMillis());
//            datePicker.setVisibility(View.VISIBLE);
//        });
        return view;
    }

    private void selectedTimeOnClickListener(View v) {
            dialogMode = DialogMode.TIME;
            relativeLayout.setVisibility(View.GONE);
            if (editCalendar !=null) {
                timePicker.setHour(editCalendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(editCalendar.get(Calendar.MINUTE));
            }
            timePicker.setVisibility(View.VISIBLE);
    }

    private void positiveButtonOnClickListener(View v) {
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
                timeSet = true;
                break;
            case CALENDAR:
                editCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                editCalendar.set(Calendar.MONTH, datePicker.getMonth());
                editCalendar.set(Calendar.YEAR, datePicker.getYear());
                dateSet = true;
                selectedDate.setText(dateFormat.format(editCalendar.getTimeInMillis()));
                datePicker.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                dialogMode = DialogMode.MAIN;
                break;
            default:
                //Repository.REPOSITORY.setTask
                dismiss();
        }
    }

    private void negativeButtonOnClickListener(View v) {
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
                dismiss();
        }
    }
}
