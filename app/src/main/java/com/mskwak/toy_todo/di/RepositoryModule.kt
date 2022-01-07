package com.mskwak.toy_todo.di

import com.mskwak.toy_todo.database.TaskDao
import com.mskwak.toy_todo.repository.DefaultTaskRepository
import com.mskwak.toy_todo.repository.TaskRepository
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