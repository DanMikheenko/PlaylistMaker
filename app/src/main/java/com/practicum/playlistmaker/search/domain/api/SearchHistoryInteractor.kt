package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun readSearchHistory(consumer: SearchHistoryConsumer)
    fun addNewTrackToHistory(track: Track)
    fun clear()

    interface SearchHistoryConsumer{
        fun consume(trackHistory: List<Track>)
    }
}