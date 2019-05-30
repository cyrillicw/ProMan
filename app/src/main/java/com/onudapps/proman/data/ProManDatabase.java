package com.onudapps.proman.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.onudapps.proman.data.db.ProManTypeConverters;
import com.onudapps.proman.data.db.entities.*;

@Database(entities = {TaskDBEntity.class, GroupDBEntity.class, ParticipantDBEntity.class, BoardDBEntity.class,
        TaskParticipantJoin.class, LastUpdateEntity.class, BoardParticipantJoin.class},
        version = 6, exportSchema = false)
@TypeConverters(value = {ProManTypeConverters.class})
public abstract class ProManDatabase extends RoomDatabase {
    public abstract ProManDao getProManDao();
}
