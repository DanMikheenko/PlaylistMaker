package com.practicum.playlistmaker.settings.domain.api

interface ThemeSettingsRepository {
    fun isDarkThemeEnabled(): Boolean
    fun switchTheme(isEnabled:Boolean)
    fun getInitialTheme()
}