package com.example.kanbanscheduler.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table WHERE topic_name= :topicName ORDER BY date, time")
    LiveData<List<Task>> getTasks(String topicName);

    @Query("UPDATE task_table SET task_type = :taskType WHERE tid= :taskId")
    void updateTaskType(int taskType, int taskId);

    @Query("UPDATE task_table SET task_name = :taskName, task_description= :description, date = :date, time = :time WHERE tid = :tid")
    void updateTask(String taskName, String description, Date date, String time, int tid);

    @Query("SELECT * FROM task_table WHERE topic_name=:topicName AND (date BETWEEN :startDate AND :endDate)")
    List<Task> getDateRangeTasks(String topicName, Date startDate, Date endDate);

    @Query("SELECT COUNT(tid) FROM task_table WHERE task_type=0 AND (date BETWEEN :startDate AND :endDate)")
    int getTotalTodos(Date startDate, Date endDate);

    @Query("SELECT COUNT(tid) FROM task_table WHERE task_type=1 AND (date BETWEEN :startDate AND :endDate)")
    int getTotalDones(Date startDate, Date endDate);


}
