package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.onudapps.proman.R;
import com.onudapps.proman.ui.listeners.DateDialogListener;
import com.onudapps.proman.viewmodels.DateDialogViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateDialogFragment extends DialogFragment {
    public static final int START_DIALOG_REQUEST_CODE = 1;
    public static final int FINISH_DIALOG_REQUEST_CODE = 2;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
    private static final String DATE_TIME_TAG = "datetime";
    private static final String REQUEST_TYPE_TAG = "requesType";

    public enum CalendarType {
        START, FINISH;
    }

    private enum DialogMode {
        MAIN, CALENDAR, TIME;
    }

    private TextView selectedDate;
    private TextView selectedTime;

    private int requestType;
    private DateDialogViewModel viewModel;

    public static DateDialogFragment newInstance(int requestType, Calendar calendar) {
        Bundle args = new Bundle();
        args.putInt(REQUEST_TYPE_TAG, requestType);
        args.putLong(DATE_TIME_TAG, calendar == null ? -1:calendar.getTimeInMillis());
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (d, w) -> {})
                .setView(R.layout.alert_date).create();
        dialog.show();
        requestType = getArguments().getInt(REQUEST_TYPE_TAG);
        Calendar calendar;
        long datetime = getArguments().getLong(DATE_TIME_TAG);
        boolean dateSet;
        boolean timeSet;
        if (datetime == -1) {
            calendar = null;
            timeSet = false;
            dateSet = false;
        }
        else {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(datetime);
            timeSet = true;
            dateSet = true;
        }
        viewModel = ViewModelProviders.of(this,
                new DateDialogViewModel.DateDialogModelFactory(calendar, dateSet, timeSet)).get(DateDialogViewModel.class);
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this::positiveOnClickListener);
        selectedDate = dialog.findViewById(R.id.selected_date);
        selectedTime = dialog.findViewById(R.id.selected_time);
        selectedTime.setOnClickListener(this::selectedTimeOnClickListener);
        selectedDate.setOnClickListener(this::selectedDateOnClickListener);
        updateSelectedDate();
        updateSelectedTime();
        ImageView dateClear = dialog.findViewById(R.id.clear);
        dateClear.setOnClickListener(this::clearOnClickListener);
        return dialog;
    }

    private void clearOnClickListener(View v) {
        viewModel.setCalendar(null);
        viewModel.setTimeSet(false);
        viewModel.setDateSet(false);
        updateSelectedDate();
        updateSelectedTime();
    }

    private void positiveOnClickListener(View v) {
        boolean dateSet = viewModel.isDateSet();
        boolean timeSet = viewModel.isTimeSet();
        if (timeSet && dateSet || !timeSet && !dateSet) {
            if (getTargetFragment() != null) {
                ((DateDialogListener)getTargetFragment()).onDateSet(requestType, viewModel.getCalendar());
            }
            else {
                ((DateDialogListener)getActivity()).onDateSet(requestType, viewModel.getCalendar());
            }
            dismiss();
        }
    }

    private void selectedTimeOnClickListener(View v) {
        final boolean is24Hours = DateFormat.is24HourFormat(getContext());
        final TimeDialog timePicker = TimeDialog.newInstance(viewModel.getCalendar(), is24Hours);
        timePicker.setListener((view, hourOfDay, minute) -> {
            if (viewModel.getCalendar() == null) {
                viewModel.setCalendar(Calendar.getInstance());
            }
            viewModel.getCalendar().set(Calendar.HOUR_OF_DAY, hourOfDay);
            viewModel.getCalendar().set(Calendar.MINUTE, minute);
            viewModel.setTimeSet(true);
            updateSelectedTime();
        });
        timePicker.showNow(getChildFragmentManager(), null);
    }

    private void selectedDateOnClickListener(View v) {
        DateDialog datePicker = DateDialog.newInstance(viewModel.getCalendar());
        datePicker.setListener((view, year, month, day) -> {
            if (viewModel.getCalendar() == null) {
                viewModel.setCalendar(Calendar.getInstance());
            }
            viewModel.getCalendar().set(Calendar.DAY_OF_MONTH, day);
            viewModel.getCalendar().set(Calendar.MONTH, month);
            viewModel.getCalendar().set(Calendar.YEAR, year);
            viewModel.setDateSet(true);
            updateSelectedDate();
        });
        datePicker.showNow(getChildFragmentManager(), null);
    }

    private void updateSelectedDate() {
        if (!viewModel.isDateSet()) {
            selectedDate.setText(R.string.date_is_not_set);
        }
        else {
            selectedDate.setText(dateFormat.format(viewModel.getCalendar().getTime()));
        }
    }

    private void updateSelectedTime() {
        if (!viewModel.isTimeSet()) {
            selectedTime.setText(R.string.time_is_not_set);
        }
        else {
            selectedTime.setText(timeFormat.format(viewModel.getCalendar().getTime()));
        }
    }
}
