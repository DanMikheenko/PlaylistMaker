package com.practicum.playlistmaker.search.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Success(val data: List<Track>): SearchState
    object NothingFound : SearchState
    object Error: SearchState
    object ConnectionError : SearchState
}