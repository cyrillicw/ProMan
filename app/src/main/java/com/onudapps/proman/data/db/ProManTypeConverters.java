package com.onudapps.proman.data.db;

import androidx.room.TypeConverter;

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
}
