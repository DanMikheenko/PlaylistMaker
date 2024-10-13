package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun search(query: String) {
        _state.postValue(State.LoadingSearchingTracks)
        tracksInteractor.searchTracks(query, object :
            TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if (errorMessage.isNullOrEmpty()) {
                    if (foundTracks?.isEmpty() == true) {
                        _state.postValue(State.ShowEmptyResult)
                    } else {
                        _state.postValue(foundTracks?.let { State.ShowSearchResult(it) })
                    }
                }
                if (errorMessage == "Проверьте подключение к интернету"){
                    _state.postValue(State.ConnectionError)
                }
                if (errorMessage == "Ошибка сервера"){
                    _state.postValue(State.Error)
                }
            }
        })
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return SearchViewModel(
                    searchHistoryInteractor = Creator.provideSearchHistoryInteractor(),
                    tracksInteractor = Creator.provideTracksInteractor()
                ) as T
            }
        }
    }

}