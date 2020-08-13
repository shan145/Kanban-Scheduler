package com.example.kanbanscheduler;

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

    @Query("SELECT * FROM task_table WHERE task_type= :taskType AND email_address= :email ORDER BY date, time")
    LiveData<List<Task>> getTasks(String taskType, String email);

    @Query("UPDATE task_table SET task_type = :taskType WHERE email_address= :email AND tid= :taskId")
    void updateTaskType(String taskType, String email, int taskId);

    @Query("UPDATE task_table SET task_name = :taskName, task_description= :description, date = :date, time = :time WHERE email_address= :emailAddress AND tid = :tid")
    void updateTask(String taskName, String description, Date date, String time, String emailAddress, int tid);
}
