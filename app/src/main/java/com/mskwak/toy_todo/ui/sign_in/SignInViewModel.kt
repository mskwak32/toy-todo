package com.mskwak.toy_todo.ui.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.util.SingleLiveEvent
import com.mskwak.toy_todo.util.TextUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

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
        email.value?.let {
            auth.fetchSignInMethodsForEmail(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNewUser = task.result.signInMethods?.isEmpty() ?: true
                    if (isNewUser) {
                        _isNewUser.value = true
                    } else {
                        _onNextEvent.call()
                    }
                } else {
                    Log.w(TAG, "fetch email: fail", task.exception)
                    when (task.exception) {
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

        auth.createUserWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "create user with email : success")
                    _onSaveNewUserEvent.call()
                } else {
                    Log.w(TAG, "create user with email : failed", task.exception)
                    when (task.exception) {
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

    companion object {
        private val TAG = SignInViewModel::class.simpleName
    }
}