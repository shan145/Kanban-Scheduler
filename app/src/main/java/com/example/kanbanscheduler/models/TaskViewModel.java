package com.example.kanbanscheduler.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kanbanscheduler.room_db.KanbanRepository;
import com.example.kanbanscheduler.room_db.Task;

import java.util.Date;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private KanbanRepository mRepository;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new KanbanRepository(application);
    }

    public LiveData<List<Task>> getTasks(String topicName) { return mRepository.getTasks(topicName);}

    public void insertTask(Task task) { mRepository.insertTask(task);}

    public void deleteTask(Task task) { mRepository.deleteTask(task);}

    public void updateTaskType(int taskType, int taskId) { mRepository.updateTaskType(taskType, taskId);}

    public void updateTask(String taskName, String description, Date date, String time, int tid) { mRepository.updateTask(taskName, description, date, time, tid); }

    public List<Task> getDateRangeTasks(String topicName, Date startDate, Date endDate) { return mRepository.getDateRangeTasks(topicName, startDate, endDate); }
    public int getTotalTodos(Date startDate, Date endDate){ return mRepository.getTotalTodos(startDate, endDate); }
    public int getTotalDones(Date startDate, Date endDate) { return mRepository.getTotalDones(startDate, endDate); }
}
