package com.practicum.playlistmaker.settings.domain.api

interface ThemeSettingsInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun switchTheme(isEnabled:Boolean)
    fun getInitialTheme()
}