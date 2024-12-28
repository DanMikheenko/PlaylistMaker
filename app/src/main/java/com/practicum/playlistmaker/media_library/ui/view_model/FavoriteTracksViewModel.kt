package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {
    private val _state = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState> get() = _state

    fun loadData() {
        viewModelScope.launch {
            val favoriteTracks = favoriteTracksInteractor.getAllFavoriteTracks()
            favoriteTracks.collect() { tracks ->
                if (tracks.isEmpty()) _state.postValue(FavoriteTracksState.ShowPlaceholder)
                else {
                    var resultTracks = mutableListOf<Track>()
                    for(track in tracks){
                        resultTracks.add(Track(
                            track.trackId,
                            track.trackName,
                            track.previewUrl,
                            track.artistName,
                            track.trackTimeMillis,
                            track.artworkUrl100,
                            track.collectionName,
                            track.releaseDate,
                            track.primaryGenreName,
                            track.country,
                            isFavorite = true
                        ))
                    }
                    _state.postValue(FavoriteTracksState.ShowFavoriteTracks(resultTracks.reversed()))
                }


            }
        }
    }
}