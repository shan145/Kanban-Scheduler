package com.example.kanbanscheduler.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TopicDao {
    @Insert
    void insert(Topic topic);

    @Delete
    void delete(Topic topic);

    @Query("UPDATE topic_table SET topic_name = :topicName WHERE id= :topicId")
    void updateTopicName(String topicName, int topicId);

    @Query("SELECT * FROM topic_table")
    LiveData<List<Topic>> getTopics();
}
