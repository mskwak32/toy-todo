package com.mskwak.toy_todo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.ui.sign_in.SignInActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        goToSignInActivity()
    }

    private fun goToSignInActivity() = lifecycleScope.launch {
        delay(1000)
        val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }
}