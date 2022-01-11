package com.mskwak.toy_todo.database.remote

import com.mskwak.toy_todo.model.Task

interface RemoteDataSource {
    suspend fun insertTask(newId: Long, task: Task)
    suspend fun updateTask(task: Task)
    suspend fun updateCompleted(taskId: Long, completed: Boolean)
    suspend fun deleteTask(task: Task)
    suspend fun deleteCompletedTask()
    suspend fun getTaskById(taskId: Long): Task?
}