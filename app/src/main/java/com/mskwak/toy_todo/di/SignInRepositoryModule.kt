package com.mskwak.toy_todo.di

import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.repository.SignInRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl): SignInRepository
}