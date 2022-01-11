package com.mskwak.toy_todo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mskwak.toy_todo.database.local.TaskDao
import com.mskwak.toy_todo.database.remote.RemoteDataSource
import com.mskwak.toy_todo.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val remoteSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    override suspend fun insertTask(task: Task) = withContext(dispatcher) {
        val id = taskDao.insertTask(task)
        remoteSource.insertTask(id, task)
    }

    override fun observeActiveTasks(): LiveData<List<Task>> {
        return taskDao.observeActiveTasks()
    }

    override fun observeCompletedTasks(): LiveData<List<Task>> {
        return taskDao.observeCompletedTasks()
    }

    override suspend fun updateTask(task: Task) = withContext(dispatcher) {
        taskDao.updateTask(task)
        remoteSource.updateTask(task)
    }

    override suspend fun getTaskById(taskId: Long): Result<Task> = withContext(dispatcher) {
        taskDao.getTaskById(taskId)?.let {
            Result.success(it)
        } ?: Result.failure(Exception("task not found"))
    }

    override suspend fun updateCompleted(taskId: Long, completed: Boolean) =
        withContext(dispatcher) {
            taskDao.updateCompleted(taskId, completed)
            remoteSource.updateCompleted(taskId, completed)
        }

    override suspend fun deleteTask(task: Task) = withContext(dispatcher) {
        taskDao.deleteTask(task)
        remoteSource.deleteTask(task)
    }

    override suspend fun deleteCompletedTasks() = withContext(dispatcher) {
        taskDao.deleteCompletedTasks()
        remoteSource.deleteCompletedTask()
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