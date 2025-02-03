package com.practicum.playlistmaker.media_library.ui.view_model

import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.domain.models.Playlist

class PlaylistEditingViewModel(playlistInteractor: PlaylistInteractor) : PlaylistCreationViewModel(
    playlistInteractor
) {
    fun loadData(playlist: Playlist){

    }
}