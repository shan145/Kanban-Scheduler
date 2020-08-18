package com.example.kanbanscheduler.room_db;

public class Topic {
    private String topicName;
    public Topic(String name) {
        topicName = name;
    }
    public String getTopicName() {
        return topicName;
    }
    public void setTopicName(String name) {
        this.topicName = name;
    }
}
