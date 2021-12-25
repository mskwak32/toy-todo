package com.mskwak.toy_todo.util

object TextUtil {
    fun checkEmailFormat(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}