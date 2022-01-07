package com.mskwak.toy_todo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mskwak.toy_todo.database.TaskDao
import com.mskwak.toy_todo.model.Task

class DefaultTaskRepository(
    private val taskDao: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    override fun observeActiveTasks(): LiveData<List<Task>> {
        return taskDao.observeActiveTasks()
    }

    override fun observeCompletedTasks(): LiveData<List<Task>> {
        return taskDao.observeCompletedTasks()
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun getTaskById(taskId: Long): Result<Task> {
        return taskDao.getTaskById(taskId)?.let {
            Result.success(it)
        } ?: Result.failure(Exception("task not found"))
    }

    override suspend fun updateCompleted(taskId: Long, completed: Boolean) {
        taskDao.updateCompleted(taskId, completed)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }

    override fun observeTaskById(taskId: Long): LiveData<Result<Task>> {
        return taskDao.observeTaskById(taskId).map {
            if (it != null) {
                Result.success(it)
            } else {
                Result.failure(Exception("task not found"))
            }
        }
    }
}