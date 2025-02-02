package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlaylistsDetailsState>()
    val state: LiveData<PlaylistsDetailsState> get() = _state

    private val _playlistDuration = MutableLiveData<Int>()
    val playlistDuration: LiveData<Int> get() = _playlistDuration

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    fun loadData(playlistId: String) {
        val id = playlistId.toIntOrNull() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                if (playlist != null) {
                    _state.postValue(PlaylistsDetailsState.Result(playlist))
                    calculatePlaylistDuration(playlist)
                } else {
                    _state.postValue(PlaylistsDetailsState.Error)
                }
            }
        }
    }

    fun calculatePlaylistDuration(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val tracksIds = playlist.addedTracksId.split(" ").filter { it.isNotBlank() }
            var tracksDuration = 0

            val durations = tracksIds.mapNotNull { id ->
                val trackId = id.toIntOrNull() ?: return@mapNotNull null
                playlistInteractor.getTrackById(trackId)
            }.map { flow ->
                flow.firstOrNull()?.trackTimeMillis?.toInt() ?: 0
            }

            tracksDuration = durations.sum()

            _playlistDuration.postValue(tracksDuration)
        }
    }

    fun loadPlaylistTracks(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val tracksIds = playlist.addedTracksId.split(" ").filter { it.isNotBlank() }
            val tracks = tracksIds.mapNotNull { id ->
                val trackId = id.toIntOrNull() ?: return@mapNotNull null
                playlistInteractor.getTrackById(trackId)
            }.map { flow ->
                flow.firstOrNull()
            }
            if (tracks.isNullOrEmpty()){
                _tracks.postValue(emptyList())
            } else{
                _tracks.postValue(tracks as List<Track>?)
            }
        }
    }
}