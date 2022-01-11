package com.mskwak.toy_todo.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mskwak.toy_todo.model.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DB_NAME = "task.db"
    }
}