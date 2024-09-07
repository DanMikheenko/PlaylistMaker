package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun readSearchHistory(): List<Track>
    fun addNewTrackToHistory(track: Track)
    fun clear()
}