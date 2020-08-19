package com.example.kanbanscheduler.room_db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class KanbanRepository {
    private TaskDao mTaskDao;
    private TopicDao mTopicDao;

    public KanbanRepository(Application application) {
        KanbanRoomDatabase db = KanbanRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mTopicDao = db.topicDao();
    }

    /*
     * Methods below are derived from TaskDao
     */
    // Observed LiveData will notify observer when data has changed
    public LiveData<List<Task>> getTasks(String taskType, String email) {
        return mTaskDao.getTasks(taskType, email);
    }
    public void insertTask(Task task) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }
    public void deleteTask(Task task) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }
    public void updateTaskType(String taskType, String email, int taskId) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTaskType(taskType, email, taskId);
        });
    }
    public void updateTask(String taskName, String description, Date date, String time, String emailAddress, int tid) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTask(taskName, description, date, time, emailAddress, tid);
        });
    }

    /*
     * Methods below are derived from TopicDao
     */
    public LiveData<List<Topic>> getTopics() {
        return mTopicDao.getTopics();
    }
    public void insertTopic(Topic topic) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTopicDao.insert(topic);
        });
    }
    public void deleteTopic(Topic topic) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTopicDao.delete(topic);
        });
    }
    public void updateTopicName(String topicName, int topicId) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTopicDao.updateTopicName(topicName, topicId);
        });
    }

}
