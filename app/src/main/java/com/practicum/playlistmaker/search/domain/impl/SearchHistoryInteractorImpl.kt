package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(val searchHistoryRepository: SearchHistoryRepository):
    SearchHistoryInteractor {
    override suspend fun readSearchHistory(consumer: SearchHistoryInteractor.SearchHistoryConsumer) {
        consumer.consume(searchHistoryRepository.readSearchHistory())
    }

    override suspend fun addNewTrackToHistory(track: Track) {
        searchHistoryRepository.addNewTrackToHistory(track)
    }

    override fun clear() {
        searchHistoryRepository.clear()
    }
}