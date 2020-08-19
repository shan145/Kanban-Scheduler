package com.example.kanbanscheduler.room_db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_table", indices = {@Index(value={"topic_name"}, unique=true)})
public class Topic {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name="topic_name")
    private String topicName;

    public Topic(@NonNull String topicName) {
        this.topicName = topicName;
    }

    @NonNull
    public String getTopicName() { return this.topicName; }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
}
