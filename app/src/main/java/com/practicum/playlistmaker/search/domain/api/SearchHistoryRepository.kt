package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    fun readSearchHistory(): List<Track>
    fun addNewTrackToHistory(track: Track)
    fun clear()
}