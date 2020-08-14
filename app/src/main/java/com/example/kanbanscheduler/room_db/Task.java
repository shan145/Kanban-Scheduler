package com.example.kanbanscheduler.room_db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName="task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int tid;

    @NonNull
    @ColumnInfo(name="email_address")
    private String email;

    @NonNull
    @ColumnInfo(name="task_name")
    private String name;

    @ColumnInfo(name="task_description")
    private String description;

    @ColumnInfo(name="date")
    @TypeConverters(DateConverters.class)
    private Date date;

    @ColumnInfo(name="time")
    @TypeConverters(TimeConverters.class)
    private String time;

    @NonNull
    @ColumnInfo(name="task_type")
    private String taskType;

    public Task(@NonNull String email, @NonNull String name, String description, Date date, String time, @NonNull String taskType) {
        this.email=email;
        this.name=name;
        this.description=description;
        this.date=date;
        this.time=time;
        this.taskType=taskType;
    }

    public int getTid() { return this.tid; }
    public void setTid(int tid) {this.tid=tid;}

    @NonNull
    public String getEmail() { return this.email; }

    @NonNull
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDate() {
        return this.date;
    }

    public String getDateString() {
        if(this.date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return dateFormat.format(this.date);
        }
        return "";
    }

    public String getTime() {
        return this.time;
    }

    @NonNull
    public String getTaskType() { return this.taskType; }
}
