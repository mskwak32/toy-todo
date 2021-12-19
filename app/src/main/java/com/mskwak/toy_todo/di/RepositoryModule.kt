package com.mskwak.toy_todo.di

import com.mskwak.toy_todo.data.DefaultTaskRepository
import com.mskwak.toy_todo.data.TaskRepository
import com.mskwak.toy_todo.database.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDefaultTaskRepository(taskDao: TaskDao): TaskRepository {
        return DefaultTaskRepository(taskDao)
    }
}