package com.mskwak.toy_todo.ui.sign_in

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.mskwak.toy_todo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setupActionBar()
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
}