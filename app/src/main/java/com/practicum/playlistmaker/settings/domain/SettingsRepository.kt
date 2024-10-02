package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun switchTheme()
    fun shareApp()
    fun openTerms()
    fun openSupport()
}