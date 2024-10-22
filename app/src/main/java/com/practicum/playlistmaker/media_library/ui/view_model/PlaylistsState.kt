package com.practicum.playlistmaker.media_library.ui.view_model

sealed interface PlaylistsState {
    data object ShowPlaceholder : PlaylistsState
}