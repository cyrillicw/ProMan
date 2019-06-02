package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class DateDialogViewModel extends ViewModel {
    private Calendar calendar;
    private boolean dateSet;
    private boolean timeSet;

    private DateDialogViewModel(Calendar calendar, boolean dateSet, boolean timeSet) {
        this.calendar = calendar;
        this.dateSet = dateSet;
        this.timeSet = timeSet;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isDateSet() {
        return dateSet;
    }

    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    public boolean isTimeSet() {
        return timeSet;
    }

    public void setTimeSet(boolean timeSet) {
        this.timeSet = timeSet;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public static class DateDialogModelFactory extends ViewModelProvider.NewInstanceFactory {

        private Calendar calendar;
        private boolean dateSet;
        private boolean timeSet;

        public DateDialogModelFactory(Calendar calendar, boolean dateSet, boolean timeSet) {
            super();
            this.calendar = calendar;
            this.dateSet = dateSet;
            this.timeSet = timeSet;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == DateDialogViewModel.class) {
                return (T) new DateDialogViewModel(calendar, dateSet, timeSet);
            }
            return null;
        }
    }
}
