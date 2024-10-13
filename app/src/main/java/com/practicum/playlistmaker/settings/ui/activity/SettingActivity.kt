package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels { SettingsViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setUI()
    }

    private fun setUI() {
        val homeButton = findViewById<View>(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        val shareTextView = findViewById<View>(R.id.shareTextView)
        shareTextView.setOnClickListener {
            viewModel.shareApp()
        }

        val writeTextView = findViewById<View>(R.id.writeToSpprtTextView)
        writeTextView.setOnClickListener {
            viewModel.openSupport()
        }

        val readAgrTextView = findViewById<View>(R.id.readAgreementTextView)
        readAgrTextView.setOnClickListener {
            viewModel.openTerms()
        }

        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)

        viewModel.isDarkThemeEnabled().observe(this, Observer { isEnabled ->
            themeSwitch.isChecked = isEnabled
        })

        themeSwitch.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.switchTheme(isEnabled)
        }
    }
}