package com.onudapps.proman.viewmodels;

import com.onudapps.proman.data.Repository;

import java.util.Calendar;

public interface StartFinishViewModelSupport {
    void updateStart(Calendar calendar);

    void updateFinish(Calendar calendar);

    Calendar getStartChanged();

    Calendar getFinishChanged();

    void setStartChanged(Calendar startChanged);

    void setFinishChanged(Calendar finishChanged);
}
