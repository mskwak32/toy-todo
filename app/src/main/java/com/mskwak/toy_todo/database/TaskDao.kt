package com.mskwak.toy_todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mskwak.toy_todo.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM taskTable WHERE isCompleted = 0")
    fun observeActiveTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM taskTable WHERE isCompleted = 1")
    fun observeCompletedTasks(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM taskTable WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("UPDATE taskTable SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Long, completed: Boolean)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM taskTable WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()
}