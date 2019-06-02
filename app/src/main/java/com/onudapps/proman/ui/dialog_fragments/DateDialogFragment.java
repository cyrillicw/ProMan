package com.onudapps.proman.ui.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
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
    private static final String LOG_TAG = "DateDialogFragment";
    public static final String RETURN_TAG = "selectedCalendar";
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

    private boolean dateSet;
    private boolean timeSet;
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
                new DateDialogViewModel.DateDialogModelFactory(calendar)).get(DateDialogViewModel.class);
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
//        confirmPressed = false;
//        datetimeLayout = view.findViewById(R.id.datetime_layout);
//        selectedDate = view.findViewById(R.id.selected_date);
//        selectedTime = dialog.findViewById(R.id.selected_time);
//        selectedTime.setOnClickListener((v) -> {
//            final Calendar c = Calendar.getInstance();
//            final boolean is24Hours = DateFormat.is24HourFormat(getContext());
//            final TimeDialog timePicker = TimeDialog.newInstance(
//                    c.get(Calendar.HOUR_OF_DAY),
//                    c.get(Calendar.MINUTE),
//                    is24Hours);
//            timePicker.setListener(null);
//            timePicker.showNow(getChildFragmentManager(), null);
//        });
        return dialog;
    }

    private void clearOnClickListener(View v) {
        viewModel.setCalendar(null);
        timeSet = false;
        dateSet = false;
        updateSelectedDate();
        updateSelectedTime();
    }

    private void positiveOnClickListener(View v) {
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
            timeSet = true;
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
            dateSet = true;
            updateSelectedDate();
        });
        datePicker.showNow(getChildFragmentManager(), null);
    }

    private void updateSelectedDate() {
        if (!dateSet) {
            selectedDate.setText(R.string.date_is_not_set);
        }
        else {
            selectedDate.setText(dateFormat.format(viewModel.getCalendar().getTime()));
        }
    }

    private void updateSelectedTime() {
        Log.e(LOG_TAG, "time set " + timeSet);
        if (!timeSet) {
            selectedTime.setText(R.string.time_is_not_set);
        }
        else {
            selectedTime.setText(timeFormat.format(viewModel.getCalendar().getTime()));
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.alert_date, container);
////        dialogMode = DialogMode.MAIN;
////        calendarType = CalendarType.valueOf(getArguments().getString(CALENDAR_TYPE_TAG));
////        Calendar calendar;
////        long datetime = getArguments().getLong(DATE_TIME_TAG);
////        if (datetime == -1) {
////            calendar = null;
////        }
////        else {
////            calendar = Calendar.getInstance();
////            calendar.setTimeInMillis(datetime);
////        }
////        DateDialogViewModel dateDialogViewModel = ViewModelProviders.of(this,
////                new DateDialogViewModel.DateDialogModelFactory(calendar)).get(DateDialogViewModel.class);
//////        confirmPressed = false;
//////        datetimeLayout = view.findViewById(R.id.datetime_layout);
//////        selectedDate = view.findViewById(R.id.selected_date);
////        selectedTime = view.findViewById(R.id.selected_time);
////        selectedTime.setOnClickListener((v) -> {
////            final Calendar c = Calendar.getInstance();
////            final boolean is24Hours = DateFormat.is24HourFormat(getContext());
////            final TimeDialog timePicker = TimeDialog.newInstance(
////                    c.get(Calendar.HOUR_OF_DAY),
////                    c.get(Calendar.MINUTE),
////                    is24Hours);
////            timePicker.setListener(null);
////            timePicker.showNow(getChildFragmentManager(), null);
////        });
////        if (getCalendar() != null) {
////            selectedDate.setText(dateFormat.format(getCalendar().getTime()));
////            selectedTime.setText(timeFormat.format(getCalendar().getTime()));
////            dateSet = true;
////            timeSet = true;
////        }
////        else {
////            selectedDate.setText("DATE IS NOT SET");
////            selectedTime.setText("TIME IS NOT SET");
////            dateSet = false;
////            timeSet = false;
////        }
////        datePicker = view.findViewById(R.id.calendar);
////        timePicker = view.findViewById(R.id.time);
////        timePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
////        TextView positiveButton = view.findViewById(R.id.confirm_button);
////        TextView negativeButton = view.findViewById(R.id.cancel_button);
////        positiveButton.setOnClickListener(this::positiveButtonOnClickListener);
////        negativeButton.setOnClickListener(this::negativeButtonOnClickListener);
////        selectedTime.setOnClickListener(this::selectedTimeOnClickListener);
////        selectedDate.setOnClickListener(this::selectedDateOnClickListener);
////        selectedDate.setOnClickListener(view -> {
////            dialogMode = TaskActivity.DialogMode.CALENDAR;
////            relativeLayout.setVisibility(View.GONE);
////            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
////            // calendarView.setDate(calendar.getTimeInMillis());
////            datePicker.setVisibility(View.VISIBLE);
////        });
//        return view;
//    }

//    private void selectedTimeOnClickListener(View v) {
//        dialogMode = DialogMode.TIME;
//        datetimeLayout.setVisibility(View.GONE);
//        if (getCalendar() !=null) {
//            timePicker.setHour(getCalendar().get(Calendar.HOUR_OF_DAY));
//            timePicker.setMinute(getCalendar().get(Calendar.MINUTE));
//        }
//        timePicker.setVisibility(View.VISIBLE);
//    }

//    private void positiveButtonOnClickListener(View v) {
//        switch (dialogMode) {
//            case TIME:
//                int hour = timePicker.getHour();
//                int minute = timePicker.getMinute();
//                if (getCalendar() == null) {
//                    initializeCalendar();
//                }
//                getCalendar().set(Calendar.HOUR_OF_DAY, hour);
//                getCalendar().set(Calendar.MINUTE, minute);
//                selectedTime.setText(timeFormat.format(getCalendar().getTime()));
//                timePicker.setVisibility(View.GONE);
//                datetimeLayout.setVisibility(View.VISIBLE);
//                dialogMode = DialogMode.MAIN;
//                timeSet = true;
//                break;
//            case CALENDAR:
//                if (getCalendar() == null) {
//                    initializeCalendar();
//                }
//                getCalendar().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
//                getCalendar().set(Calendar.MONTH, datePicker.getMonth());
//                getCalendar().set(Calendar.YEAR, datePicker.getYear());
//                dateSet = true;
//                selectedDate.setText(dateFormat.format(getCalendar().getTimeInMillis()));
//                datePicker.setVisibility(View.GONE);
//                datetimeLayout.setVisibility(View.VISIBLE);
//                dialogMode = DialogMode.MAIN;
//                break;
//            default:
//                updateCalendar();
//        }
//    }

//    private void selectedDateOnClickListener(View v) {
//        dialogMode = DialogMode.CALENDAR;
//        datetimeLayout.setVisibility(View.GONE);
//        if (getCalendar() != null) {
//            datePicker.updateDate(getCalendar().get(Calendar.YEAR),
//                    getCalendar().get(Calendar.MONTH),
//                    getCalendar().get(Calendar.DAY_OF_MONTH));
//        }
//
//        // calendarView.setDate(calendar.getTimeInMillis());
//        datePicker.setVisibility(View.VISIBLE);
//    }
//
//    private void negativeButtonOnClickListener(View v) {
//        switch (dialogMode) {
//            case TIME:
//                timePicker.setVisibility(View.GONE);
//                datetimeLayout.setVisibility(View.VISIBLE);
//                dialogMode = DialogMode.MAIN;
//                break;
//            case CALENDAR:
//                datePicker.setVisibility(View.GONE);
//                datetimeLayout.setVisibility(View.VISIBLE);
//                dialogMode = DialogMode.MAIN;
//                break;
//            default:
//                dismiss();
//        }
//    }

//    private Calendar getCalendar() {
//        if (calendarType == CalendarType.START) {
//            return viewModel.getStartChanged();
//        }
//        else {
//            return viewModel.getFinishChanged();
//        }
//    }
//
//    private void initializeCalendar() {
//        if (calendarType == CalendarType.START) {
//            viewModel.setStartChanged(Calendar.getInstance());
//        }
//        else {
//            viewModel.setFinishChanged(Calendar.getInstance());
//        }
//    }
//    private void updateCalendar() {
//        if (timeSet && dateSet) {
//            if (calendarType == CalendarType.START)
//                viewModel.updateStart(getCalendar());
//            else {
//                viewModel.updateFinish(getCalendar());
//            }
//            dismiss();
//            Toast.makeText(getContext(), getResources().getString(R.string.update_alert), Toast.LENGTH_LONG).show();
//        }
//        else if (!timeSet && !dateSet) {
//            if (calendarType == CalendarType.START)
//                viewModel.updateStart(null);
//            else {
//                viewModel.updateFinish(null);
//            }
//            dismiss();
//            Toast.makeText(getContext(), getResources().getString(R.string.update_alert), Toast.LENGTH_LONG).show();
//        }
//    }
}
