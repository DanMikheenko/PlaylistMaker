package com.practicum.playlistmaker.search.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface State {
    object LoadingSearchingTracks : State
    data class ShowSearchResult(val data: List<Track>): State
    object ShowEmptyResult : State
    object Error: State
    object ConnectionError : State
    data class ShowSearchingTrackHistory(val data: List<Track>): State
    object ShowEmptyTrackHistory: State
}