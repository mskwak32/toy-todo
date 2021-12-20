package com.mskwak.toy_todo

import com.mskwak.toy_todo.model.Task

object MockTaskUtil {

    fun mockTask() = Task(
        title = "mockTitle",
        memo = "mockMemo"
    )

    fun mockTasks() = listOf(
        mockTask(),
        mockTask(),
        mockTask()
    )
}