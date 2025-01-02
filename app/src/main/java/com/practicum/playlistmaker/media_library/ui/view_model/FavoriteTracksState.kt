package com.practicum.playlistmaker.media_library.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoriteTracksState {
    data object ShowPlaceholder : FavoriteTracksState
    data class ShowFavoriteTracks(val data: List<Track>): FavoriteTracksState
}