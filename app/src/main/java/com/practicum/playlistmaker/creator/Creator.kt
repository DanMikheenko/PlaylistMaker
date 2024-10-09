package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.data.ThemeSettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.ThemeSettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigation
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingRepository

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getSharingRepository(context: Context): SharingRepository{
        return ExternalNavigation(context)
    }

    private fun getThemeSettingsRepository(context: Context) : ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(context)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(context))
    }

    fun provideThemeSettingsInteractor(context: Context): ThemeSettingsInteractor{
        return ThemeSettingsInteractorImpl(getThemeSettingsRepository(context))
    }


}