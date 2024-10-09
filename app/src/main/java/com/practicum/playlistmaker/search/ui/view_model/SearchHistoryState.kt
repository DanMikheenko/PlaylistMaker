package com.practicum.playlistmaker.search.ui.view_model

sealed interface SearchHistoryState {
    object Empty : SearchHistoryState
    object NotEmpty : SearchHistoryState
}