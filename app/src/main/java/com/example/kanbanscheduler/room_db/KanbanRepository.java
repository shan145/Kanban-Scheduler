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
    public LiveData<List<Task>> getTasks(String topicName) {
        return mTaskDao.getTasks(topicName);
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
    public void updateTaskType(int taskType, int taskId) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTaskType(taskType, taskId);
        });
    }
    public void updateTask(String taskName, String description, Date date, String time, int tid) {
        KanbanRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTask(taskName, description, date, time, tid);
        });
    }
    public List<Task> getDateRangeTasks(String topicName, Date startDate, Date endDate) {
        return mTaskDao.getDateRangeTasks(topicName, startDate, endDate);
    }
    public LiveData<Integer> getTotalTodos(Date startDate, Date endDate) {
        return mTaskDao.getTotalTodos(startDate, endDate);
    }
    public LiveData<Integer> getTotalDones(Date startDate, Date endDate) {
        return mTaskDao.getTotalDones(startDate, endDate);
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
