package com.example.kanbanscheduler;

public class Task {
    private String name;
    private String description;
    private String date;
    private String time;

    Task(String name, String description, String date, String time) {
        this.name=name;
        this.description=description;
        this.date=date;
        this.time=time;
    }

    String getName() {return this.name;}
    String getDescription() {return this.description;}
    String getDate() {return this.date;}
    String getTime() {return this.time;}
}
