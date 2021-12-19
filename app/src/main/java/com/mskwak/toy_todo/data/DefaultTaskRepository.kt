package com.mskwak.toy_todo.data

import androidx.lifecycle.LiveData
import com.mskwak.toy_todo.database.TaskDao
import com.mskwak.toy_todo.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
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
        } ?: Result.failure(NoSuchElementException("task not fount"))
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
}