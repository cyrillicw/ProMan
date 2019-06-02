package com.onudapps.proman.ui.dialog_fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateDialog extends DialogFragment {
    private static final String ARGUMENT_DAY = "ARGUMENT_DAY";
    private static final String ARGUMENT_MONTH = "ARGUMENT_MONTH";
    private static final String ARGUMENT_YEAR = "ARGUMENT_YEAR";
    private DatePickerDialog.OnDateSetListener listener;

    private int day;
    private int month;
    private int year;

    public static DateDialog newInstance(Calendar calendar) {
        final DateDialog df = new DateDialog();
        final Bundle args = new Bundle();
        Calendar argsCalendar = calendar == null ? Calendar.getInstance() : calendar;
        args.putInt(ARGUMENT_DAY, argsCalendar.get(Calendar.DAY_OF_MONTH));
        args.putInt(ARGUMENT_MONTH, argsCalendar.get(Calendar.MONTH));
        args.putInt(ARGUMENT_YEAR, argsCalendar.get(Calendar.YEAR));
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
            day = args.getInt(ARGUMENT_DAY);
            month = args.getInt(ARGUMENT_MONTH);
            year = args.getInt(ARGUMENT_YEAR);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        return new DatePickerDialog(getContext(), listener, year, month, day);
        //return new DatePickerDialog(tContext(), this.listener, this.hourOfDay, this.minute, this.is24Hours);
    }

    public void setListener(final DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
}
