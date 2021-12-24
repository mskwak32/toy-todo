package com.mskwak.toy_todo.ui.sign_in

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mskwak.toy_todo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setupActionBar()
        currentUserSignOut()
    }

    private fun setupActionBar() {
        val toolbar = this.findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerInSignIn) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerInSignIn).navigateUp() || super.onSupportNavigateUp()
    }

    private fun currentUserSignOut() {
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            auth.signOut()
        }
    }
}