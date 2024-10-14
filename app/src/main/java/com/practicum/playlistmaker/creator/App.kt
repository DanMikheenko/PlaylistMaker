package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        Creator.setApplicationContext(this)
//        val themeSettingsInteractor = Creator.provideThemeSettingsInteractor()
//        themeSettingsInteractor.getInitialTheme()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, viewModelModule)
        }
    }
}