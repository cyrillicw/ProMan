package com.onudapps.proman.data;

import android.content.Context;
import androidx.room.Room;

class LocalDataSource {
    private static final String databaseTitle = "ProManDatabase";
    private ProManDatabase database;
    LocalDataSource(Context context) {
        database = Room.databaseBuilder(context, ProManDatabase.class, databaseTitle).build();
    }
}
