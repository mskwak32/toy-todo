package com.mskwak.toy_todo.ui.sign_in.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import com.mskwak.toy_todo.util.TextUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val signInRepo: SignInRepository
) : ViewModel() {
    val email = MutableLiveData<String>(signInRepo.getCurrentEmail())

    private val _emailErrorMessage = SingleLiveEvent<Int?>()
    val emailErrorMessage: LiveData<Int?> = _emailErrorMessage
    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _onSendEvent = SingleLiveEvent<String>()        //parameter : email
    val onSendEvent: LiveData<String> = _onSendEvent

    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    fun onSend() {
        if (!TextUtil.checkEmailFormat(email.value)) {
            _emailErrorMessage.value = R.string.email_error
            return
        }

        _inProgress.value = true

        signInRepo.sendPasswordReset(email.value!!) { result ->
            result.onSuccess {
                _onSendEvent.value = email.value
            }.onFailure { e ->
                when (e) {
                    is FirebaseTooManyRequestsException -> {
                        _snackbarMessage.value = R.string.message_too_many_request_auth
                    }
                    else -> {
                        _snackbarMessage.value = R.string.message_authentication_fail
                    }
                }
            }
            _inProgress.value = false
        }
    }

    fun onEmailChanged() {
        _emailErrorMessage.value = null
    }
}