package com.onudapps.proman.data.db;

import androidx.room.TypeConverter;
import com.onudapps.proman.data.db.entities.LastUpdateEntity;

import java.util.Calendar;
import java.util.UUID;

public class ProManTypeConverters {
    @TypeConverter
    public Calendar calendarFromTimestamp(Long value) {
        if (value == null) {
            return null;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }
    }

    @TypeConverter
    public Long calendarToTimestamp(Calendar calendar) {
        if (calendar == null) {
            return null;
        } else {
            return calendar.getTimeInMillis();
        }
    }

    @TypeConverter
    public UUID uuidFromString(String value) {
        if (value == null) {
            return null;
        }
        else {
            return UUID.fromString(value);
        }
    }

    @TypeConverter
    public String uuidToString(UUID value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    @TypeConverter
    public String queryTypeToString(LastUpdateEntity.Query query) {
        return query.name();
    }

    @TypeConverter
    public LastUpdateEntity.Query queryTypeFromString(String s) {
        return LastUpdateEntity.Query.valueOf(s);
    }
}
