package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun readSearchHistory(consumer: SearchHistoryConsumer)
    fun addNewTrackToHistory(track: Track)
    fun clear()

    interface SearchHistoryConsumer{
        fun consume(trackHistory: List<Track>)
    }
}