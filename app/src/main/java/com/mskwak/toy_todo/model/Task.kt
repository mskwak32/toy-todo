package com.mskwak.toy_todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskTable")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String = "",
    var memo: String = "",
    var isCompleted: Boolean = false
) {

    fun getTitleText(): String {
        return if (title.isNotBlank()) title else memo
    }

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isBlank() && memo.isBlank()
}
