package com.mskwak.toy_todo.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mskwak.toy_todo.database.local.TaskDao
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TaskDaoTest : LocalDatabase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var taskDao: TaskDao

    @Before
    fun init() {
        taskDao = db.taskDao()
    }

    @Test
    fun observeWrongTask() = runBlocking {
        val result = taskDao.observeTaskById(100)
        assertEquals(result.value, null)
    }
}