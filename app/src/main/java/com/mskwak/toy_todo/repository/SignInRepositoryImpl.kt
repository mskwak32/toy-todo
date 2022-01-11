package com.mskwak.toy_todo.repository

import android.util.Log
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.AppApplication
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor() : SignInRepository {
    private val auth = Firebase.auth
    private var email: String? = null
        set(value) {
            AppApplication.INSTANCE.currentUserEmail = value
            field = value
        }

    //이미 로그인되어 있는지 검사
    override fun isSignIn(callback: (isSignIn: Boolean) -> Unit) {
        auth.currentUser?.reload()
            ?.addOnSuccessListener {
                callback.invoke(true)
                email = auth.currentUser?.email

            }?.addOnFailureListener {
                callback.invoke(false)
            } ?: kotlin.run {
            callback.invoke(false)
        }
    }

    override fun fetchEmail(email: String, callback: (emailExists: Result<Boolean>) -> Unit) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnSuccessListener { result ->
                val isNewUser = result.signInMethods?.isEmpty() ?: true
                if (isNewUser) {
                    callback.invoke(Result.success(false))
                } else {
                    callback.invoke(Result.success(true))
                }
                this.email = email

            }.addOnFailureListener {
                Log.w(TAG, "fetch email: fail", it)
                callback.invoke(Result.failure(it))
            }
    }

    override fun createNewUser(
        email: String,
        password: String,
        callback: (success: Result<Boolean>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d(TAG, "create user with email : success")
                callback.invoke(Result.success(true))

            }.addOnFailureListener {
                Log.w(TAG, "create user with email : failed", it)
                callback.invoke(Result.failure(it))
            }
    }

    override fun signInWithGoogle(idToken: String, callback: (success: Result<Boolean>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            Log.d(TAG, "sign in with credential: success")
            callback.invoke(Result.success(true))
            email = auth.currentUser?.email

        }.addOnFailureListener {
            Log.w(TAG, "sign in with credential: failed", it)
            callback.invoke(Result.failure(it))
        }
    }

    override fun signInWithEmail(password: String, callback: (success: Result<Boolean>) -> Unit) {
        if (email.isNullOrBlank()) {
            Log.e(TAG, "email is null")
            callback.invoke(Result.failure(Exception("email is null. fetch email first")))
            return
        }

        auth.signInWithEmailAndPassword(email!!, password)
            .addOnSuccessListener {
                Log.d(TAG, "signInWithEmail: success")
                callback.invoke(Result.success(true))

            }.addOnFailureListener {
                Log.w(TAG, "signInWithEmail: fail", it)
                callback.invoke(Result.failure(it))
            }
    }

    override fun sendPasswordReset(email: String, callback: (success: Result<Boolean>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "send recover email: success")
                callback.invoke(Result.success(true))

            }.addOnFailureListener {
                Log.w(TAG, "send recover email: fail", it)
                callback.invoke(Result.failure(it))
            }
    }

    override fun signOut() {
        auth.signOut()
        email = null
    }

    companion object {
        private val TAG = SignInRepositoryImpl::class.simpleName
    }
}