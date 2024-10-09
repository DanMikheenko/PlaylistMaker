package com.practicum.playlistmaker.settings.data

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsRepository

class ThemeSettingsRepositoryImpl(private val context: Context) : ThemeSettingsRepository {

    private companion object {
        private const val PLAY_LIST_MAKER = "playListMakerSettings"
        private const val THEME_MODE = "themeSwitcherKey"
    }

    private var darkTheme = false
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PLAY_LIST_MAKER, MODE_PRIVATE)

    init {
        getInitialTheme()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return darkTheme
    }

    override fun switchTheme(isEnabled: Boolean) {
        darkTheme = isEnabled

        sharedPreferences.edit()
            .putBoolean(THEME_MODE, darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun getInitialTheme() {
        switchTheme(
            sharedPreferences.getBoolean(
                THEME_MODE,
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            )
        )
    }
}