package com.practicum.playlistmaker.media_library.ui.view_model

import com.practicum.playlistmaker.media_library.domain.models.Playlist

sealed interface PlaylistsDetailsState {
    data object Error : PlaylistsDetailsState
    data class Result(val data: Playlist): PlaylistsDetailsState
}