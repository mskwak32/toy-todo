package com.mskwak.toy_todo.ui.main

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val auth = Firebase.auth

    val email: String
        get() = auth.currentUser?.email ?: ""

    fun signOut() {
        auth.signOut()
    }
}