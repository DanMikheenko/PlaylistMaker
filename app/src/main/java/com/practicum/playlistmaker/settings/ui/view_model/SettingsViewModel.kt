package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor : SharingInteractor,
    private val themeSettingsInteractor: ThemeSettingsInteractor
) : ViewModel() {

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

//    companion object {
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                return SettingsViewModel(
//                    sharingInteractor = Creator.provideSharingInteractor(),
//                    themeSettingsInteractor = Creator.provideThemeSettingsInteractor()
//                ) as T
//            }
//        }
//    }

}