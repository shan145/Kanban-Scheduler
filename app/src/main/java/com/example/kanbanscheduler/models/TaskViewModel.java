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

    public LiveData<List<Task>> getTasks(String taskType, String email) { return mRepository.getTasks(taskType, email);}

    public void insertTask(Task task) { mRepository.insertTask(task);}

    public void deleteTask(Task task) { mRepository.deleteTask(task);}

    public void updateTaskType(String taskType, String email, int taskId) { mRepository.updateTaskType(taskType, email, taskId);}

    public void updateTask(String taskName, String description, Date date, String time, String emailAddress, int tid) { mRepository.updateTask(taskName, description, date, time, emailAddress, tid); }
}
