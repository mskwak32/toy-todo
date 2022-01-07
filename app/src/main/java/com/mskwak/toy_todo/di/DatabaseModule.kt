package com.mskwak.toy_todo.di

import android.app.Application
import androidx.room.Room
import com.mskwak.toy_todo.database.TaskDao
import com.mskwak.toy_todo.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(application, TaskDatabase::class.java, TaskDatabase.DB_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }
}