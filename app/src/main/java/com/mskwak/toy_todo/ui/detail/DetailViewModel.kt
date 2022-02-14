package com.mskwak.toy_todo.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.repository.TaskRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class DetailViewModel @AssistedInject constructor(
    private val repository: TaskRepository,
    @Assisted private val taskId: Long
) : ViewModel() {

    var task = repository.observeTaskById(taskId).map {
        computeResult(it)
    }

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _onEditEvent = SingleLiveEvent<Unit>()
    val onEditEvent: LiveData<Unit> = _onEditEvent

    private val _onDeletedEvent = SingleLiveEvent<Unit>()
    val onDeleteEvent: LiveData<Unit> = _onDeletedEvent

    private val _onDeleteClickEvent = SingleLiveEvent<Unit>()
    val onDeleteClickEvent: LiveData<Unit> = _onDeleteClickEvent

    private fun computeResult(result: Result<Task>): Task? {
        return if (result.isSuccess) {
            result.getOrNull()
        } else {
            Log.e(TAG, result.toString())
            _snackbarMessage.value = R.string.error_load_tasks
            null
        }
    }

    fun completeTask(complete: Boolean) {
        task.value?.let {
            viewModelScope.launch {
                repository.updateCompleted(taskId, complete)
                _snackbarMessage.value =
                    if (complete) R.string.message_marked_complete else R.string.message_marked_active
            }
        }
    }

    fun openEdit() {
        _onEditEvent.call()
    }

    fun deleteTask() {
        task.value?.let {
            viewModelScope.launch {
                repository.deleteTask(it)
                _onDeletedEvent.call()
            }
        }
    }

    fun onDeleteClick() {
        _onDeleteClickEvent.call()
    }


    @AssistedFactory
    interface DetailViewModelAssistedFactory {
        fun create(taskId: Long): DetailViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: DetailViewModelAssistedFactory,
            taskId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(taskId) as T
            }
        }

        private val TAG = DetailViewModel::class.simpleName
    }
}