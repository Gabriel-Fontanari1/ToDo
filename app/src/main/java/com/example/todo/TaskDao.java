package com.example.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    List<Tasks> getAllTasks();

    @Query("SELECT * FROM tasks WHERE done = :doneStatus")
    List<Tasks> getTasksByStatus(boolean doneStatus);

    @Insert
    void insertTask(Tasks task);

    @Update
    void updateTask(Tasks task);

    @Delete
    void deleteTask(Tasks task);
}
