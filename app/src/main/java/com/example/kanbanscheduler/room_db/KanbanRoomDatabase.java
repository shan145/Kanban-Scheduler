package com.example.kanbanscheduler.room_db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Topic.class}, version=18, exportSchema = false)
@TypeConverters({DateConverters.class, TimeConverters.class})
public abstract class KanbanRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract TopicDao topicDao();

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
