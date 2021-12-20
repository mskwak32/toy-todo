package com.mskwak.toy_todo.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.data.TaskRepository
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val isEmptyList = MutableLiveData(false)

    private val _snacbarMessage = SingleLiveEvent<Int>()
    val snacbarMessage: LiveData<Int> = _snacbarMessage

    private val _openDetailEvent = SingleLiveEvent<Long>()
    val openDetailEvent: LiveData<Long> = _openDetailEvent

    val activeTasks = repository.observeActiveTasks()

    fun openDetail(task: Task) {
        _openDetailEvent.value = task.id
    }

    fun showSnackbar(@StringRes stringId: Int) {
        _snacbarMessage.value = stringId
    }

    fun updateCompleted(task: Task, completed: Boolean) {
        viewModelScope.launch {
            delay(1000)
            repository.updateCompleted(task.id, completed)
            _snacbarMessage.postValue(R.string.message_marked_complete)
        }
    }

}