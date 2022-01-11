package com.mskwak.toy_todo.repository

interface SignInRepository {
    fun isSignIn(callback: (isSignIn: Boolean) -> Unit)
    fun fetchEmail(email: String, callback: (emailExists: Result<Boolean>) -> Unit)
    fun createNewUser(email: String, password: String, callback: (success: Result<Boolean>) -> Unit)
    fun signInWithGoogle(idToken: String, callback: (success: Result<Boolean>) -> Unit)
    fun signInWithEmail(password: String, callback: (success: Result<Boolean>) -> Unit)
    fun sendPasswordReset(email: String, callback: (success: Result<Boolean>) -> Unit)
    fun signOut()
}