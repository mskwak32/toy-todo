package com.mskwak.toy_todo.ui.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInHomeViewModel @Inject constructor() : ViewModel() {
    private val auth = Firebase.auth

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage: LiveData<Int> = _snackbarMessage
    private val _onCredentialSignInEvent = SingleLiveEvent<Unit>()
    val onCredentialSignInEvent: LiveData<Unit> = _onCredentialSignInEvent

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "sign in with credential: success")
                    _onCredentialSignInEvent.call()
                } else {
                    Log.w(TAG, "sign in with credential: failed", task.exception)
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

    companion object {
        private val TAG = SignInHomeViewModel::class.simpleName
    }
}