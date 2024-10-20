package com.practicum.playlistmaker.media_library.ui.view_model

sealed interface FavoriteTracksState {
    data object ShowPlaceholder : FavoriteTracksState
}