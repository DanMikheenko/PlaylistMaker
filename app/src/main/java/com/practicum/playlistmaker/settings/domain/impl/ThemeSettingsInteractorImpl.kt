package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsRepository

class ThemeSettingsInteractorImpl(private val themeSettingsRepository: ThemeSettingsRepository): ThemeSettingsInteractor {
    override fun isDarkThemeEnabled(): Boolean{
        return themeSettingsRepository.isDarkThemeEnabled()
    }
    override fun switchTheme(isEnabled:Boolean){
        themeSettingsRepository.switchTheme(isEnabled)
    }

    override fun getInitialTheme() {
        themeSettingsRepository.getInitialTheme()
    }
}