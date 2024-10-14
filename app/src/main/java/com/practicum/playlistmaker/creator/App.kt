package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class App: Application(), KoinComponent{

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, viewModelModule)
        }

        val themeSettingsInteractor: ThemeSettingsInteractor by inject()
        themeSettingsInteractor.getInitialTheme()
    }
}