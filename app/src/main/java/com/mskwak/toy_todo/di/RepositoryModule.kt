package com.mskwak.toy_todo.di

import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.repository.SignInRepositoryImpl
import com.mskwak.toy_todo.repository.TaskRepository
import com.mskwak.toy_todo.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl): SignInRepository
}