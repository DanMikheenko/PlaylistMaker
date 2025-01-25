package com.practicum.playlistmaker.media_library.ui.view_model

import com.practicum.playlistmaker.media_library.domain.models.Playlist

sealed interface PlaylistsState {
    data object ShowPlaceholder : PlaylistsState
    data class ShowResult(val data: List<Playlist>): PlaylistsState

}