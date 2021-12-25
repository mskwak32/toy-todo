package com.mskwak.toy_todo.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.mskwak.toy_todo.R
import com.mskwak.toy_todo.ui.sign_in.SignInActivity
import com.mskwak.toy_todo.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBar()
        initNavHeader()
    }

    private fun setupActionBar() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                as NavHostFragment
        val navController = navHost.navController
        appBarConfig =
            AppBarConfiguration.Builder(R.id.homeFragmentDest, R.id.CompletedFragmentDest)
                .setOpenableLayout(drawerLayout)
                .build()
        setupActionBarWithNavController(navController, appBarConfig)
        findViewById<NavigationView>(R.id.navView).setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainer).navigateUp(appBarConfig)
                || super.onSupportNavigateUp()
    }

    fun showSnacbarMessage(message: String) {
        drawerLayout.showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    private fun initNavHeader() {
        val navigationView = findViewById<NavigationView>(R.id.navView)
        navigationView.getHeaderView(0)?.let {
            it.findViewById<TextView>(R.id.email).apply {
                text = viewModel.email
            }
            it.findViewById<Button>(R.id.signOutButton).apply {
                setOnClickListener {
                    viewModel.signOut()
                    navigateToSignInActivity()
                }
            }
        }
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}