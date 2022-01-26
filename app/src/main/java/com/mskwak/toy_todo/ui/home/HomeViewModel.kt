package com.mskwak.toy_todo.ui.home

import androidx.lifecycle.*
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.repository.TaskRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeViewModel @AssistedInject constructor(
    private val repository: TaskRepository,
    @Assisted private val isActiveTasks: Boolean
) : ViewModel() {

    val isEmptyList = MutableLiveData(false)

    private val _snacbarMessage = SingleLiveEvent<Int>()
    val snacbarMessage: LiveData<Int> = _snacbarMessage

    private val _openDetailEvent = SingleLiveEvent<Long>()
    val openDetailEvent: LiveData<Long> = _openDetailEvent

    var tasks: LiveData<List<Task>> = if (isActiveTasks) {
        repository.observeActiveTasks()
    } else {
        repository.observeCompletedTasks()
    }

    fun openDetail(task: Task) {
        _openDetailEvent.value = task.id
    }

    fun updateCompleted(task: Task, completed: Boolean) {
        viewModelScope.launch {
            delay(1000)
            repository.updateCompleted(task.id, completed)
            val stringId = if (completed) {
                R.string.message_marked_complete
            } else {
                R.string.message_marked_active
            }
            _snacbarMessage.value = stringId
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.deleteCompletedTasks()
            _snacbarMessage.value = R.string.message_completed_tasks_cleared
        }
    }

    @AssistedFactory
    interface HomeViewModelAssistedFactory {
        fun create(isActiveTasks: Boolean): HomeViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: HomeViewModelAssistedFactory,
            isActiveTasks: Boolean
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(isActiveTasks) as T
            }
        }
    }
}

internal const val EDIT_TASK_RESULT_KEY = "editTaskResultKey"