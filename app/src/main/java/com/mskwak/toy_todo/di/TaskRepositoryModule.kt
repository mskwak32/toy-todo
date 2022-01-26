package com.mskwak.toy_todo.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.database.remote.FireStoreDataSource
import com.mskwak.toy_todo.database.remote.RemoteDataSource
import com.mskwak.toy_todo.repository.TaskRepository
import com.mskwak.toy_todo.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class TaskRepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideRemoteDataSource(): RemoteDataSource {
        val db = Firebase.firestore
        val uid = Firebase.auth.currentUser?.uid ?: kotlin.run {
            throw Exception("TaskRepositoryModule: uid is null")
        }
        return FireStoreDataSource(db, uid)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideTaskRepository(remoteDataSource: RemoteDataSource): TaskRepository {
        return TaskRepositoryImpl(remoteDataSource)
    }
}