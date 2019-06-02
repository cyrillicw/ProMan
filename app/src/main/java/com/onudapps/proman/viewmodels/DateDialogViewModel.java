package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class DateDialogViewModel extends ViewModel {
    private Calendar calendar;

    private DateDialogViewModel(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public static class DateDialogModelFactory extends ViewModelProvider.NewInstanceFactory {

        private Calendar calendar;

        public DateDialogModelFactory(Calendar calendar) {
            super();
            this.calendar = calendar;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == DateDialogViewModel.class) {
                return (T) new DateDialogViewModel(calendar);
            }
            return null;
        }
    }
}
