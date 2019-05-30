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
import com.onudapps.proman.ui.activities.TaskActivity;
import com.onudapps.proman.viewmodels.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskDateDialogFragment extends DialogFragment {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private static final String DATE_TIME_TAG = "datetime";
    private static final String CALENDAR_TYPE_TAG = "calendarType";

    public enum CalendarType {
        START, FINISH;
    }

    private enum DialogMode {
        MAIN, CALENDAR, TIME;
    }

    private TimePicker timePicker;
    private DatePicker datePicker;
    private RelativeLayout datetimeLayout;
    private TextView selectedDate;
    private TextView selectedTime;

    private DialogMode dialogMode;
    private CalendarType calendarType;
    private boolean dateSet;
    private boolean timeSet;
    private boolean confirmPressed;
    private TaskViewModel viewModel;

    public static TaskDateDialogFragment newInstance(CalendarType calendarType) {
        Bundle args = new Bundle();
        args.putString(CALENDAR_TYPE_TAG, calendarType.toString());
        TaskDateDialogFragment fragment = new TaskDateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_date, container);
        dialogMode = DialogMode.MAIN;
        calendarType = CalendarType.valueOf(getArguments().getString(CALENDAR_TYPE_TAG));
        confirmPressed = false;
        datetimeLayout = view.findViewById(R.id.datetime_layout);
        selectedDate = view.findViewById(R.id.selected_date);
        selectedTime = view.findViewById(R.id.selected_time);
        viewModel = ((TaskActivity)getActivity()).getTaskViewModel();
        if (getCalendar() != null) {
            selectedDate.setText(dateFormat.format(getCalendar().getTime()));
            selectedTime.setText(timeFormat.format(getCalendar().getTime()));
            dateSet = true;
            timeSet = true;
        }
        else {
            selectedDate.setText("DATE IS NOT SET");
            selectedTime.setText("TIME IS NOT SET");
            dateSet = false;
            timeSet = false;
        }
        datePicker = view.findViewById(R.id.calendar);
        timePicker = view.findViewById(R.id.time);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        TextView positiveButton = view.findViewById(R.id.confirm_button);
        TextView negativeButton = view.findViewById(R.id.cancel_button);
        positiveButton.setOnClickListener(this::positiveButtonOnClickListener);
        negativeButton.setOnClickListener(this::negativeButtonOnClickListener);
        selectedTime.setOnClickListener(this::selectedTimeOnClickListener);
        selectedDate.setOnClickListener(this::selectedDateOnClickListener);
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
            datetimeLayout.setVisibility(View.GONE);
            if (getCalendar() !=null) {
                timePicker.setHour(getCalendar().get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(getCalendar().get(Calendar.MINUTE));
            }
            timePicker.setVisibility(View.VISIBLE);
    }

    private void positiveButtonOnClickListener(View v) {
        switch (dialogMode) {
            case TIME:
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                if (getCalendar() == null) {
                    initializeCalendar();
                }
                getCalendar().set(Calendar.HOUR_OF_DAY, hour);
                getCalendar().set(Calendar.MINUTE, minute);
                selectedTime.setText(timeFormat.format(getCalendar().getTime()));
                timePicker.setVisibility(View.GONE);
                datetimeLayout.setVisibility(View.VISIBLE);
                dialogMode = DialogMode.MAIN;
                timeSet = true;
                break;
            case CALENDAR:
                if (getCalendar() == null) {
                    initializeCalendar();
                }
                getCalendar().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                getCalendar().set(Calendar.MONTH, datePicker.getMonth());
                getCalendar().set(Calendar.YEAR, datePicker.getYear());
                dateSet = true;
                selectedDate.setText(dateFormat.format(getCalendar().getTimeInMillis()));
                datePicker.setVisibility(View.GONE);
                datetimeLayout.setVisibility(View.VISIBLE);
                dialogMode = DialogMode.MAIN;
                break;
            default:
                updateCalendar();
        }
    }

    private void selectedDateOnClickListener(View v) {
        dialogMode = DialogMode.CALENDAR;
        datetimeLayout.setVisibility(View.GONE);
        if (getCalendar() != null) {
            datePicker.updateDate(getCalendar().get(Calendar.YEAR),
                    getCalendar().get(Calendar.MONTH),
                    getCalendar().get(Calendar.DAY_OF_MONTH));
        }

        // calendarView.setDate(calendar.getTimeInMillis());
        datePicker.setVisibility(View.VISIBLE);
    }

    private void negativeButtonOnClickListener(View v) {
        switch (dialogMode) {
            case TIME:
                timePicker.setVisibility(View.GONE);
                datetimeLayout.setVisibility(View.VISIBLE);
                dialogMode = DialogMode.MAIN;
                break;
            case CALENDAR:
                datePicker.setVisibility(View.GONE);
                datetimeLayout.setVisibility(View.VISIBLE);
                dialogMode = DialogMode.MAIN;
                break;
            default:
                dismiss();
        }
    }

    private Calendar getCalendar() {
        if (calendarType == CalendarType.START) {
            return viewModel.getStartChanged();
        }
        else {
            return viewModel.getFinishChanged();
        }
    }

    private void initializeCalendar() {
        if (calendarType == CalendarType.START) {
            viewModel.setStartChanged(Calendar.getInstance());
        }
        else {
            viewModel.setFinishChanged(Calendar.getInstance());
        }
    }
    private void updateCalendar() {
        if (timeSet && dateSet) {
            if (calendarType == CalendarType.START)
                viewModel.updateStart(getCalendar());
            else {
                viewModel.updateFinish(getCalendar());
            }
            dismiss();
            Toast.makeText(getContext(), getResources().getString(R.string.update_alert), Toast.LENGTH_LONG).show();
        }
        else if (!timeSet && !dateSet) {
            if (calendarType == CalendarType.START)
                viewModel.updateStart(null);
            else {
                viewModel.updateFinish(null);
            }
            dismiss();
            Toast.makeText(getContext(), getResources().getString(R.string.update_alert), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConfirmPressed() {
        return confirmPressed;
    }
}
