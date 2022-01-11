package com.mskwak.toy_todo.di

import com.mskwak.toy_todo.database.local.TaskDao
import com.mskwak.toy_todo.database.remote.RemoteDataSource
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.repository.SignInRepositoryImpl
import com.mskwak.toy_todo.repository.TaskRepository
import com.mskwak.toy_todo.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
        remoteDataSource: RemoteDataSource
    ): TaskRepository {
        return TaskRepositoryImpl(taskDao, remoteDataSource)
    }

    @Provides
    @Singleton
    fun bindSignInRepository(): SignInRepository {
        return SignInRepositoryImpl()
    }
}