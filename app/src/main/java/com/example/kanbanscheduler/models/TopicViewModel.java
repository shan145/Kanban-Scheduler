package com.example.kanbanscheduler.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kanbanscheduler.room_db.KanbanRepository;
import com.example.kanbanscheduler.room_db.Topic;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {

    private KanbanRepository mRepository;
    public TopicViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new KanbanRepository(application);
    }

    public LiveData<List<Topic>> getTopics() { return mRepository.getTopics(); }

    public void insertTopic(Topic topic) { mRepository.insertTopic(topic); }

    public void deleteTopic(Topic topic) { mRepository.deleteTopic(topic); }

    public void updateTopicName(String topicName, int topicId) { mRepository.updateTopicName(topicName, topicId); }
}
