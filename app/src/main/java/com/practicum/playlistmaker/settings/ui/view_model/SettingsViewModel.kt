package com.practicum.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import creator.Creator
import com.practicum.playlistmaker.settings.data.ThemeSettingsModel

class SettingsViewModel(application: Context, activityContext: Context) : ViewModel() {
    private val sharingInteractor = Creator.provideSharingInteractor(application, activityContext)
    private val themeSettingsModel = ThemeSettingsModel(application)

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>().apply {
        value = themeSettingsModel.isDarkThemeEnabled()
    }

    fun isDarkThemeEnabled(): LiveData<Boolean> = _isDarkThemeEnabled

    fun switchTheme(isEnabled: Boolean) {
        themeSettingsModel.switchTheme(isEnabled)
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