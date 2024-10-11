package com.practicum.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel(context: Context) : ViewModel() {
    private val sharingInteractor = Creator.provideSharingInteractor()
    private val themeSettingsInteractor = Creator.provideThemeSettingsInteractor()

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>().apply {
        value = themeSettingsInteractor.isDarkThemeEnabled()
    }

    fun isDarkThemeEnabled(): LiveData<Boolean> = _isDarkThemeEnabled

    fun switchTheme(isEnabled: Boolean) {
        themeSettingsInteractor.switchTheme(isEnabled)
        _isDarkThemeEnabled.value = isEnabled
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}