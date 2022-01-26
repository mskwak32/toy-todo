package com.mskwak.toy_todo.model

data class Task(
    val id: Long = 0,
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
