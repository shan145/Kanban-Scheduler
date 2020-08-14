package com.example.kanbanscheduler.room_db;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeConverters {
    @TypeConverter
    public static String timestampToTime(Long value) {
        if(value != null) {
            Date currentTime = new Date(value);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            return timeFormat.format(currentTime);
        }
        return null;
    }

    @TypeConverter
    public static Long timeToTimestamp (String timeString) {
        if(timeString != null) {
            try {
                Date currentTime = new SimpleDateFormat("hh:mm a", Locale.US).parse(timeString);
                assert currentTime != null;
                return currentTime.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
