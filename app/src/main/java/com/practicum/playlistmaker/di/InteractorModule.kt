package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeSettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.ThemeSettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<ThemeSettingsInteractor> {
        ThemeSettingsInteractorImpl(get())
    }
}