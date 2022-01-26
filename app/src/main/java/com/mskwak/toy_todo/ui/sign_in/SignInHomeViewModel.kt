package com.mskwak.toy_todo.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInHomeViewModel @Inject constructor(
    private val signInRepo: SignInRepository
) : ViewModel() {
    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage
    private val _onCredentialSignInEvent = SingleLiveEvent<Unit>()
    val onCredentialSignInEvent: LiveData<Unit> = _onCredentialSignInEvent

    fun firebaseAuthWithGoogle(idToken: String) {
        signInRepo.signInWithGoogle(idToken) { result ->
            result.onSuccess {
                _onCredentialSignInEvent.call()
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
}