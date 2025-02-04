package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditingViewModel(private val playlistInteractor: PlaylistInteractor) : PlaylistCreationViewModel(
    playlistInteractor
) {

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist

    fun loadData(playlistId: String){
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(playlistId.toInt()).collect(){playlist->
                if (playlist == null){
                    _playlist.postValue(null)
                } else {
                    _playlist.postValue(playlist)
                }
            }
        }
    }
}