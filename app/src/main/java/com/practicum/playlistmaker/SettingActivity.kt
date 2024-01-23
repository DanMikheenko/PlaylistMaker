package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val homeButton = findViewById<View>(R.id.home_button)
        homeButton.setOnClickListener{
            finish()
        }

    }
}