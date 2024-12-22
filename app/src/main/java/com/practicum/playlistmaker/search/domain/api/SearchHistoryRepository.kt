package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryRepository {
    suspend fun readSearchHistory(): List<Track>
    suspend fun addNewTrackToHistory(track: Track)
    fun clear()
}