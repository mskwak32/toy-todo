package com.mskwak.toy_todo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mskwak.toy_todo.database.remote.RemoteDataSource
import com.mskwak.toy_todo.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val remoteSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    override suspend fun insertTask(task: Task) = withContext(dispatcher) {
        val id = System.currentTimeMillis()
        remoteSource.insertTask(id, task)
    }

    override fun observeActiveTasks(): LiveData<List<Task>> {
        return remoteSource.observeActiveTasks()
    }

    override fun observeCompletedTasks(): LiveData<List<Task>> {
        return remoteSource.observeCompletedTasks()
    }

    override suspend fun updateTask(task: Task) = withContext(dispatcher) {
        remoteSource.updateTask(task)
    }

    override suspend fun getTaskById(taskId: Long): Result<Task> = withContext(dispatcher) {
        remoteSource.getTaskById(taskId)?.let {
            Result.success(it)
        } ?: Result.failure(Exception("task not found"))
    }

    override suspend fun updateCompleted(taskId: Long, completed: Boolean) =
        withContext(dispatcher) {
            remoteSource.updateCompleted(taskId, completed)
        }

    override suspend fun deleteTask(task: Task) = withContext(dispatcher) {
        remoteSource.deleteTask(task)
    }

    override suspend fun deleteCompletedTasks() = withContext(dispatcher) {
        remoteSource.deleteCompletedTask()
    }

    override fun observeTaskById(taskId: Long): LiveData<Result<Task>> {
        return remoteSource.observeTaskById(taskId).map { Result.success(it) }
    }
}