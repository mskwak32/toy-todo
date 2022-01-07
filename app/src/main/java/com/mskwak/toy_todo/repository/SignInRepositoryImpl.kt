package com.mskwak.toy_todo.repository

import android.util.Log
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor() : SignInRepository {
    private val auth = Firebase.auth
    private var email: String? = null

    //이미 로그인되어 있는지 검사
    override fun isSignIn(callback: (isSignIn: Boolean) -> Unit) {
        auth.currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback.invoke(true)
                email = auth.currentUser?.email
            } else {
                callback.invoke(false)
            }
        } ?: kotlin.run {
            callback.invoke(false)
        }
    }

    override fun fetchEmail(email: String, callback: (emailExists: Result<Boolean>) -> Unit) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isNewUser = task.result.signInMethods?.isEmpty() ?: true
                if (isNewUser) {
                    callback.invoke(Result.success(false))
                } else {
                    callback.invoke(Result.success(true))
                }
                this.email = email
            } else {
                Log.w(TAG, "fetch email: fail", task.exception)
                task.exception?.let { callback.invoke(Result.failure(it)) }
            }
        }
    }

    override fun createNewUser(
        email: String,
        password: String,
        callback: (success: Result<Boolean>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "create user with email : success")
                callback.invoke(Result.success(true))
            } else {
                Log.w(TAG, "create user with email : failed", task.exception)
                task.exception?.let { callback.invoke(Result.failure(it)) }
            }
        }
    }

    override fun signInWithGoogle(idToken: String, callback: (success: Result<Boolean>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "sign in with credential: success")
                callback.invoke(Result.success(true))
                email = auth.currentUser?.email
            } else {
                Log.w(TAG, "sign in with credential: failed", task.exception)
                task.exception?.let { callback.invoke(Result.failure(it)) }
            }
        }
    }

    override fun signInWithEmail(password: String, callback: (success: Result<Boolean>) -> Unit) {
        val email = getCurrentEmail() ?: kotlin.run {
            Log.e(TAG, "email is null")
            callback.invoke(Result.failure(Exception("email is null. fetch email first")))
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithEmail: success")
                callback.invoke(Result.success(true))
            } else {
                Log.w(TAG, "signInWithEmail: fail", task.exception)
                task.exception?.let { callback.invoke(Result.failure(it)) }
            }
        }
    }

    override fun sendPasswordReset(email: String, callback: (success: Result<Boolean>) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "send recover email: success")
                callback.invoke(Result.success(true))
            } else {
                Log.w(TAG, "send recover email: fail", task.exception)
                task.exception?.let { callback.invoke(Result.failure(it)) }
            }
        }
    }

    override fun getCurrentEmail(): String? = email

    override fun signOut() {
        auth.signOut()
    }

    companion object {
        private val TAG = SignInRepositoryImpl::class.simpleName
    }
}