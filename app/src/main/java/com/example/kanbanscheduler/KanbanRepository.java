package com.example.kanbanscheduler;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class KanbanRepository {
    private TaskDao mTaskDao;

    KanbanRepository(Application application) {
        KanbanRoomDatabase db = KanbanRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
    }

    // Observed LiveData will notify observer when data has changed
    LiveData<List<Task>> getTasks(String taskType, String email) {
        return mTaskDao.getTasks(taskType, email);
    }

    void insertTask(Task task) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    void deleteTask(Task task) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }

    void updateTaskType(String taskType, String email, int taskId) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTaskType(taskType, email, taskId);
        });
    }

    void updateTask(String taskName, String description, Date date, String time, String emailAddress, int tid) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTask(taskName, description, date, time, emailAddress, tid);
        });
    }

}
