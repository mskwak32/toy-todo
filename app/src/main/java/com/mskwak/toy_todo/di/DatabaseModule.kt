package com.mskwak.toy_todo.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.database.local.TaskDao
import com.mskwak.toy_todo.database.local.TaskDatabase
import com.mskwak.toy_todo.database.remote.FireStoreDataSource
import com.mskwak.toy_todo.database.remote.RemoteDataSource
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

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        val setting = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        val db = Firebase.firestore.apply {
            firestoreSettings = setting
        }
        return FireStoreDataSource(db)
    }
}