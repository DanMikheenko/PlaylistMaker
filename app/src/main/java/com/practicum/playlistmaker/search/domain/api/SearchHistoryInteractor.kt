package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    suspend fun readSearchHistory(consumer: SearchHistoryConsumer)
    suspend fun addNewTrackToHistory(track: Track)
    fun clear()

    interface SearchHistoryConsumer{
        fun consume(trackHistory: List<Track>)
    }
}