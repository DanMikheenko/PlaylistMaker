package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.data.ThemeSettingsModel

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val themeSettingsModel = ThemeSettingsModel(application)

    private val _isDarkThemeEnabled = MutableLiveData<Boolean>().apply {
        value = themeSettingsModel.isDarkThemeEnabled()
    }
    fun isDarkThemeEnabled(): LiveData<Boolean> = _isDarkThemeEnabled

    fun switchTheme(isEnabled: Boolean) {
        themeSettingsModel.switchTheme(isEnabled)
        _isDarkThemeEnabled.value = isEnabled
    }

}