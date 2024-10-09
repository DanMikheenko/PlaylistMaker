package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _tracksHistory = MutableLiveData<List<Track>>()
    val tracksHistory: LiveData<List<Track>> = _tracksHistory

    private val _state = MutableLiveData<SearchState>()
    val state : LiveData<SearchState> = _state

    fun search(query: String) {
        _state.postValue(SearchState.Loading)
        _tracks.postValue(emptyList())
        tracksInteractor.searchTracks(query, object :
            TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                if (foundTracks.isEmpty()){
                    _state.postValue(SearchState.NothingFound)
                } else{
                    _state.postValue(SearchState.Success(foundTracks))
                    _tracks.postValue(foundTracks)
                }
            }
        })
    }

    fun readSearchHistory(){
        searchHistoryInteractor.readSearchHistory(object :
            SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(trackHistory: List<Track>) {
                _tracksHistory.postValue(trackHistory)
            }
        })

    }

    fun isSearchHistoryEmpty(): Boolean {
        var isEmpty = true
        searchHistoryInteractor.readSearchHistory(object :
            SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(trackHistory: List<Track>) {
                if (trackHistory.isEmpty()) {
                    isEmpty = true
                } else {
                    isEmpty = false
                }
            }
        })
        return isEmpty
    }

    fun clearTracks(){
        _tracks.postValue(emptyList())
    }

    fun clearHistory(){
        searchHistoryInteractor.clear()
    }


}