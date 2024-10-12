package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
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
    const val PLAY_LIST_MAKER_SHARE_PREFERENCES = "playListMakerSettings"
    private lateinit var _appContext: Context

    fun setApplicationContext(context: Context) {
        _appContext = context
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl()
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getSharingRepository(): SharingRepository {
        return ExternalNavigation(_appContext)
    }

    private fun getThemeSettingsRepository(): ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(_appContext)
    }

    fun getSharedPreferences(): SharedPreferences {
        return _appContext.getSharedPreferences(PLAY_LIST_MAKER_SHARE_PREFERENCES, MODE_PRIVATE)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }

    fun provideThemeSettingsInteractor(): ThemeSettingsInteractor {
        return ThemeSettingsInteractorImpl(getThemeSettingsRepository())
    }


}