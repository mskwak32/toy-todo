package com.mskwak.toy_todo.ui.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _isNewUser = MutableLiveData(false)
    val isNewUser: LiveData<Boolean> = _isNewUser

    private val _isEmainError = MutableLiveData<Boolean>()
    val isEmailError: LiveData<Boolean> = _isEmainError
    private val _isPasswordError = MutableLiveData<Boolean>()
    val isPasswordError: LiveData<Boolean> = _isPasswordError

    private val _onNextEvent = SingleLiveEvent<String>()
    val onNextEvent: LiveData<String> = _onNextEvent
    private val _onSaveNewUserEvent = SingleLiveEvent<Unit>()
    val onSaveNewUserEvent: LiveData<Unit> = _onSaveNewUserEvent

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun onNext() {
        if (email.value.isNullOrBlank() || !checkEmailFormat()) {
            _isEmainError.value = true
            return
        }
        email.value?.let {
            auth.fetchSignInMethodsForEmail(it).addOnCompleteListener { task ->
                try {
                    val isNewUser = task.result.signInMethods?.isEmpty() ?: true
                    if (isNewUser) {
                        _isNewUser.value = true
                    } else {
                        _onNextEvent.value = email.value
                    }
                } catch (e: RuntimeExecutionException) {
                    _snackbarMessage.value = R.string.message_authentication_fail
                    Log.w(TAG, e.toString())
                }
            }
        }
    }

    private fun checkEmailFormat(): Boolean {
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return pattern.matcher(email.value!!).matches()
    }

    fun onSaveNewUser() {
        if (email.value.isNullOrBlank() || password.value.isNullOrBlank()) {
            _isEmainError.value = email.value.isNullOrBlank()
            _isPasswordError.value = password.value.isNullOrBlank()
            return
        }
        val email = email.value ?: throw Exception("email is null")
        val password = password.value ?: throw Exception("password is null")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "create user with email : success")
                    _onSaveNewUserEvent.call()
                } else {
                    Log.w(TAG, "create user with email : failed")
                    _snackbarMessage.value = R.string.message_authentication_fail
                }
            }
    }

    fun onEmailTextChange() {
        _isNewUser.value = false
        _isEmainError.value = false
    }

    fun onPasswordTextChanged() {
        _isPasswordError.value = false
    }

    companion object {
        private val TAG = SignInViewModel::class.simpleName
    }
}