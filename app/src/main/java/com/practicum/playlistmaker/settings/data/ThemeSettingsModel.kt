package com.practicum.playlistmaker.settings.data

import android.content.Context

class ThemeSettingsModel(private val context: Context) {
    private var _isDarkThemeEnabled: Boolean = (context as App).isDarkThemeEnabled()
    fun isDarkThemeEnabled(): Boolean {
        return _isDarkThemeEnabled
    }
    fun switchTheme(isEnabled:Boolean) {
        (context as App).switchTheme(isEnabled)
        _isDarkThemeEnabled = isEnabled
    }
}