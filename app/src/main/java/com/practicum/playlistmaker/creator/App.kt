package com.practicum.playlistmaker.creator

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.setApplicationContext(this)
        val themeSettingsInteractor = Creator.provideThemeSettingsInteractor()
        themeSettingsInteractor.getInitialTheme()
    }
}