package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {
    private val _state = MutableLiveData<State>(State.ShowEmptyTrackHistory)
    val state: LiveData<State> = _state

    fun search(query: String) {
        _state.postValue(State.LoadingSearchingTracks)
        if (!query.isNullOrEmpty()) {
            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect() { pair ->
                        if (pair.second.isNullOrEmpty()) {
                            if (pair.first?.isEmpty() == true) {
                                _state.postValue(State.ShowEmptyResult)
                            } else {
                                _state.postValue(State.ShowSearchResult(pair.first!!))
                            }
                        }
                        if (pair.second == "Проверьте подключение к интернету") {
                            _state.postValue(State.ConnectionError)
                        }
                        if (pair.second == "Ошибка сервера") {
                            _state.postValue(State.Error)
                        }
                    }
            }
        }
    }

    fun readSearchHistory() {
        searchHistoryInteractor.readSearchHistory(object :
            SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(trackHistory: List<Track>) {
                if (trackHistory.isEmpty()) {
                    _state.postValue(State.ShowEmptyTrackHistory)
                } else {
                    _state.postValue(State.ShowSearchingTrackHistory(trackHistory))
                }

            }
        })
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
        _state.postValue(State.ShowEmptyTrackHistory)
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.addNewTrackToHistory(track)
    }
}