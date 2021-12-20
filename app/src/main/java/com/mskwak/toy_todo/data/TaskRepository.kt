package com.mskwak.toy_todo.data

import androidx.lifecycle.LiveData
import com.mskwak.toy_todo.model.Task

interface TaskRepository {

    suspend fun insertTask(task: Task)

    fun observeActiveTasks(): LiveData<List<Task>>

    fun observeCompletedTasks(): LiveData<List<Task>>

    suspend fun updateTask(task: Task)

    suspend fun getTaskById(taskId: Long): Result<Task>

    suspend fun updateCompleted(taskId: Long, completed: Boolean)

    suspend fun deleteTask(task: Task)

    suspend fun deleteCompletedTasks()

    fun observeTaskById(taskId: Long): LiveData<Result<Task>>
}