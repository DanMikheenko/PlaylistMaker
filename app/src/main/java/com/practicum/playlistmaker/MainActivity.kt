package com.practicum.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Слушатель для изменения видимости BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistCreationFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }


        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            bottomNavigationView.visibility = if (imeVisible) View.GONE else View.VISIBLE
            insets
        }
    }

    fun hideBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).animate()
            .translationY(findViewById<BottomNavigationView>(R.id.bottom_navigation).height.toFloat())
            .setDuration(300)
            .start()
        findViewById<View>(R.id.separator).visibility = View.GONE
    }

    fun showBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).animate()
            .translationY(0f)
            .setDuration(300)
            .start()
        findViewById<View>(R.id.separator).visibility = View.VISIBLE
    }
}