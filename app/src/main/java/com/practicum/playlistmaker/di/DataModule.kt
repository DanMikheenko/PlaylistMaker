package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.ITunesSearchAPI
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.settings.data.ThemeSettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigation
import com.practicum.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchAPI::class.java)
    }
    single {
        androidContext()
            .getSharedPreferences("playListMakerSettings", Context.MODE_PRIVATE)
    }
    factory { Gson() }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }


    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single { MediaPlayer() }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<SharingRepository> {
        ExternalNavigation(get())
    }

    single<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(get())
    }
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
}