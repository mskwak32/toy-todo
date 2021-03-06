package com.mskwak.toy_todo.ui.sign_in.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val signInRepo: SignInRepository
) : ViewModel() {

    val password = MutableLiveData<String>()

    private val _pwErrorMessage = MutableLiveData<Int?>()
    val pwErrorMessage: LiveData<Int?> = _pwErrorMessage
    private val _recoverPwEvent = SingleLiveEvent<Unit>()
    val recoverPwEvent: LiveData<Unit> = _recoverPwEvent
    private val _signInEvent = SingleLiveEvent<Unit>()
    val signInEvent: LiveData<Unit> = _signInEvent
    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> = _inProgress

    fun onSignIn() {
        if (password.value.isNullOrBlank()) {
            _snackbarMessage.value = R.string.enter_your_password
            return
        }

        _inProgress.value = true
        signInRepo.signInWithEmail(password.value!!) { result ->
            result.onSuccess {
                _signInEvent.call()
            }.onFailure { e ->
                when (e) {
                    //password not matched
                    is FirebaseAuthInvalidCredentialsException -> {
                        _pwErrorMessage.value = R.string.password_not_matched
                    }
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

    fun onRecoverPw() {
        _recoverPwEvent.call()
    }

    fun onPasswordChanged() {
        _pwErrorMessage.value = null
    }
}