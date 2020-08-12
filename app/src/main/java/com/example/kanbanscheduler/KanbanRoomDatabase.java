package com.example.kanbanscheduler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version=3, exportSchema = false)
public abstract class KanbanRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static volatile KanbanRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static KanbanRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (KanbanRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), KanbanRoomDatabase.class, "kanban_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}