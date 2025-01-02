package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media_library.ui.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(
            playerInteractor = get(),
            track = track,
            favoriteTracksInteractor = get()
        )
    }

    viewModel{
        SearchViewModel(get(), get())
    }
    viewModel{
        SettingsViewModel(get(), get())
    }
    viewModel{
        FavoriteTracksViewModel(get())
    }
    viewModel{
        PlaylistsViewModel()
    }

}