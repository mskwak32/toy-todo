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

    companion object {
        const val FIELD_TITLE = "title"
        const val FIELD_MEMO = "memo"
        const val FIELD_COMPLETED = "completed"
    }
}
