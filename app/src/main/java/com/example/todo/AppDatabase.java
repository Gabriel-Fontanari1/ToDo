package com.example.todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todo.TaskDao;
import com.example.todo.Tasks;

@Database(entities = {Tasks.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
