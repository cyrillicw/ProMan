package com.onudapps.proman.data;

import android.content.Context;
import androidx.room.Room;
import com.onudapps.proman.data.pojo.Task;

import java.util.UUID;

class LocalDataSource {
    private static final String databaseTitle = "ProManDatabase";
    private ProManDatabase database;
    LocalDataSource(Context context) {
        database = Room.databaseBuilder(context, ProManDatabase.class, databaseTitle).build();
    }

    public Task getTask(UUID id) {
        return database.getProManDao().getTask(id);
    }

    public void insertTask(Task task) {
        database.getProManDao().insertTask(task);
    }
}
