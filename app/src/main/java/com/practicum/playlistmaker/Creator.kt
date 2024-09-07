package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }
    private fun getPlayerRepository(): PlayerRepository{
        return PlayerRepositoryImpl()
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences) : SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))
    }
    fun providePlayerInteractor(): PlayerInteractor{
        return PlayerInteractorImpl(getPlayerRepository())
    }
}