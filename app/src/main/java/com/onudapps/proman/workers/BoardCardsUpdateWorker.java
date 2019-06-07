package com.onudapps.proman.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.onudapps.proman.data.Repository;

public class BoardCardsUpdateWorker extends Worker {
    public BoardCardsUpdateWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    @NonNull
    @Override
    public Result doWork() {
        Repository.REPOSITORY.updateBoardCards();
        return null;
    }
}
