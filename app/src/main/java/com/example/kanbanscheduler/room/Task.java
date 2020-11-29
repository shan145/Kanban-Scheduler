package com.example.kanbanscheduler.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName="task_table", foreignKeys = @ForeignKey(entity=Topic.class, parentColumns = "topic_name", childColumns = "topic_name", onDelete = ForeignKey.CASCADE), indices = {@Index("topic_name")})
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int tid;

    @NonNull
    @ColumnInfo(name="topic_name")
    private String topic;

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
    private int taskType;

    public Task(@NonNull String topic, @NonNull String name, String description, Date date, String time, @NonNull int taskType) {
        this.topic=topic;
        this.name=name;
        this.description=description;
        this.date=date;
        this.time=time;
        this.taskType=taskType;
    }

    public int getTid() { return this.tid; }
    public void setTid(int tid) {this.tid=tid;}

    @NonNull
    public String getTopic() { return this.topic; }

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
    public int getTaskType() { return this.taskType; }
}
