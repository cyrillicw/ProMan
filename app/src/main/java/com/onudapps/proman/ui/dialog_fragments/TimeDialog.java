package com.onudapps.proman.ui.dialog_fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimeDialog extends DialogFragment {

    private static final String ARGUMENT_HOUR = "ARGUMENT_HOUR";
    private static final String ARGUMENT_MINUTE = "ARGUMENT_MINUTE";
    private static final String ARGUMENT_IS_24_HOURS = "ARGUMENT_IS_24_HOURS";
    private TimePickerDialog.OnTimeSetListener listener;

    private int hourOfDay;
    private int minute;
    private boolean is24Hours;

    public static TimeDialog newInstance(Calendar calendar, boolean is24Hours) {
        final TimeDialog df = new TimeDialog();
        final Bundle args = new Bundle();
        Calendar argsCalendar = calendar == null ? Calendar.getInstance() : calendar;
        args.putInt(ARGUMENT_HOUR, argsCalendar.get(Calendar.HOUR_OF_DAY));
        args.putInt(ARGUMENT_MINUTE, argsCalendar.get(Calendar.MINUTE));
        args.putBoolean(ARGUMENT_IS_24_HOURS, is24Hours);
        df.setArguments(args);
        return df;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveArguments();
    }

    private void retrieveArguments() {
        final Bundle args = getArguments();
        if (args != null) {
            hourOfDay = args.getInt(ARGUMENT_HOUR);
            minute = args.getInt(ARGUMENT_MINUTE);
            is24Hours = args.getBoolean(ARGUMENT_IS_24_HOURS);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new TimePickerDialog(getContext(), this.listener, this.hourOfDay, this.minute, this.is24Hours);
    }

    public void setListener(final TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }
}