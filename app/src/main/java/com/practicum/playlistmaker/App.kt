package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private companion object {
        private const val PLAY_LIST_MAKER = "playListMakerSettings"
        private const val THEME_MODE = "themeSwitcherKey"
    }

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAY_LIST_MAKER, MODE_PRIVATE)
        switchTheme(
            sharedPreferences.getBoolean(
                THEME_MODE,
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            )
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        sharedPreferences.edit()
            .putBoolean(THEME_MODE, darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkThemeEnabled(): Boolean {
        return darkTheme
    }
}