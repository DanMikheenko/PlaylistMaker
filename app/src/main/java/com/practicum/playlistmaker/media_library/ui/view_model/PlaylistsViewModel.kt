package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> get() = _state

    fun loadData() {
        viewModelScope.launch {
            playlistInteractor.getAll().collect() { playlists ->
                if (playlists.isNullOrEmpty()){
                    _state.postValue(PlaylistsState.ShowPlaceholder)
                } else{
                    _state.postValue(PlaylistsState.ShowResult(playlists))
                }
            }
        }
    }
}