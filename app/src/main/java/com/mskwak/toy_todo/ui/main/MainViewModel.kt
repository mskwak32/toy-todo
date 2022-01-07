package com.mskwak.toy_todo.ui.main

import androidx.lifecycle.ViewModel
import com.mskwak.toy_todo.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val signInRepo: SignInRepository
) : ViewModel() {

    val email: String
        get() = signInRepo.getCurrentEmail() ?: ""

    fun signOut() {
        signInRepo.signOut()
    }
}