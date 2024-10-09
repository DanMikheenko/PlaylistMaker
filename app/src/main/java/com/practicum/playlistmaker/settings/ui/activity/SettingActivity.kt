package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.settings.ui.view_model.ViewModelFactory

class SettingActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        // Передаем ApplicationContext в фабрику
        val factory = ViewModelFactory(applicationContext)

        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        setUI()
    }

    private fun setUI() {
        val homeButton = findViewById<View>(R.id.home_button)
        homeButton.setOnClickListener {
            finish()
        }

        val shareTextView = findViewById<View>(R.id.shareTextView)
        shareTextView.setOnClickListener {
            share()
        }

        val writeTextView = findViewById<View>(R.id.writeToSpprtTextView)
        writeTextView.setOnClickListener {
            writeToSupport()
        }

        val readAgrTextView = findViewById<View>(R.id.readAgreementTextView)
        readAgrTextView.setOnClickListener {
            readUserAgreement()
        }

        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)

        viewModel.isDarkThemeEnabled().observe(this, Observer { isEnabled ->
            themeSwitch.isChecked = isEnabled
        })

        themeSwitch.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.switchTheme(isEnabled)
        }
    }

    private fun share() {
        viewModel.shareApp()
    }

    private fun writeToSupport() {
        viewModel.openSupport()
    }

    private fun readUserAgreement() {
        viewModel.openTerms()
    }
}