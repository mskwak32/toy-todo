package com.mskwak.toy_todo.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import com.mskwak.toy_todo.util.TextUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepo: SignInRepository
) : ViewModel() {

    private val _isNewUser = MutableLiveData(false)
    val isNewUser: LiveData<Boolean> = _isNewUser

    private val _emailErrorMessage = SingleLiveEvent<Int?>()
    val emailErrorMessage: LiveData<Int?> = _emailErrorMessage
    private val _pwErrorMessage = MutableLiveData<Int?>()
    val pwErrorMessage: LiveData<Int?> = _pwErrorMessage

    private val _onNextEvent = SingleLiveEvent<Unit>()
    val onNextEvent: LiveData<Unit> = _onNextEvent
    private val _onSaveNewUserEvent = SingleLiveEvent<Unit>()
    val onSaveNewUserEvent: LiveData<Unit> = _onSaveNewUserEvent

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun onNext() {
        if (email.value.isNullOrBlank() || !TextUtil.checkEmailFormat(email.value)) {
            _emailErrorMessage.value = R.string.email_error
            return
        }

        signInRepo.fetchEmail(email.value!!) { result ->
            result.onSuccess { emailExists ->
                if (emailExists) {
                    _onNextEvent.call()
                } else {
                    _isNewUser.value = true
                }
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
        }
    }

    fun onSaveNewUser() {
        var error = false
        if (email.value.isNullOrBlank()) {
            _emailErrorMessage.value = R.string.email_error
            error = true
        }
        if (password.value.isNullOrBlank()) {
            _pwErrorMessage.value = R.string.enter_your_password
            error = true
        }

        if (error) return

        signInRepo.createNewUser(email.value!!, password.value!!) { result ->
            result.onSuccess {
                _onSaveNewUserEvent.call()
            }.onFailure { e ->
                when (e) {
                    is FirebaseAuthWeakPasswordException -> {
                        _pwErrorMessage.value = R.string.new_password_error
                    }
                    else -> {
                        _snackbarMessage.value = R.string.message_authentication_fail
                    }
                }
            }
        }
    }

    fun onEmailTextChange() {
        _isNewUser.value = false
        _emailErrorMessage.value = null
    }

    fun onPasswordTextChanged() {
        _pwErrorMessage.value = null
    }
}