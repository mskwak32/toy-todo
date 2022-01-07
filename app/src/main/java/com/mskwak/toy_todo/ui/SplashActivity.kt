package com.mskwak.toy_todo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.repository.SignInRepository
import com.mskwak.toy_todo.ui.main.MainActivity
import com.mskwak.toy_todo.ui.sign_in.SignInActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var signInRepo: SignInRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(1000)
            checkCurrentUser()
        }
    }

    //로그인 되어있는 지 검사 후 다음 Activity로 이동
    private fun checkCurrentUser() {
        signInRepo.isSignIn { isSignIn ->
            if (isSignIn) {
                navigateToMainActivity()
            } else {
                navigateToSignInActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }
}