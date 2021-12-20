package com.mskwak.toy_todo.ui.edit

import android.util.Log
import androidx.lifecycle.*
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.data.TaskRepository
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditViewModel @AssistedInject constructor(
    private val repository: TaskRepository,
    @Assisted val taskId: Long?
) : ViewModel() {
    private var task: Task? = null

    val title = MutableLiveData<String>()
    val memo = MutableLiveData<String>()

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _onAddEvent = SingleLiveEvent<Unit>()
    val onAddEvent: LiveData<Unit> = _onAddEvent

    private val _onUpdateEvent = SingleLiveEvent<Unit>()
    val onUpdateEvent: LiveData<Unit> = _onUpdateEvent

    init {
        taskId?.let {
            viewModelScope.launch {
                repository.getTaskById(it).onSuccess {
                    withContext(Dispatchers.Main) {
                        task = it
                        title.value = it.title
                        memo.value = it.memo
                    }
                }.onFailure {
                    Log.e(TAG, it.toString())
                    _snackbarMessage.value = R.string.error_load_tasks
                }
            }
        }
    }

    fun saveTask() {
        if (title.value.isNullOrBlank() && memo.value.isNullOrBlank()) {
            _snackbarMessage.value = R.string.message_empty_task
            return
        }
        viewModelScope.launch {
            task?.let {                 //update
                it.title = title.value ?: ""
                it.memo = memo.value ?: ""
                repository.updateTask(it)
                _onUpdateEvent.call()
            } ?: kotlin.run {            //save new
                val task = Task(title = title.value ?: "", memo = memo.value ?: "")
                repository.insertTask(task)
                _onAddEvent.call()
            }
        }
    }

    @AssistedFactory
    interface EditViewModelAssistedFactory {
        fun create(taskId: Long?): EditViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: EditViewModelAssistedFactory,
            taskId: Long?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(taskId) as T
            }
        }

        private val TAG = EditViewModel::class.simpleName
    }
}