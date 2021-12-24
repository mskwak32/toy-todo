package com.mskwak.toy_todo.ui.sign_in.password

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.util.SingleLiveEvent
import com.mskwak.toy_todo.util.TextUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor() : ViewModel() {
    private val auth = Firebase.auth

    val email = MutableLiveData<String>(auth.currentUser?.email)

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

        auth.sendPasswordResetEmail(email.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "send recover email: success")
                    _onSendEvent.value = email.value
                } else {
                    Log.w(TAG, "send recover email: fail", task.exception)
                    when (task.exception) {
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

    companion object {
        private val TAG = RecoverPasswordViewModel::class.simpleName
    }
}