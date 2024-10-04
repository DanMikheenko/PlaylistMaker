package com.practicum.playlistmaker.settings.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val appContext: Context,
    private val activityContext: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(appContext, activityContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}