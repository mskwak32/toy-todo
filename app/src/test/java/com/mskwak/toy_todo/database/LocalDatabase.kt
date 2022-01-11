package com.mskwak.toy_todo.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mskwak.toy_todo.database.local.TaskDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
abstract class LocalDatabase {
    lateinit var db: TaskDatabase

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }
}