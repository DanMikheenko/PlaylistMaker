package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _state = MutableLiveData<PlaylistsDetailsState>()
    val state: LiveData<PlaylistsDetailsState> get() = _state

    fun loadData(playlistId: String){
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(playlistId.toInt()).collect(){playlist->
                if (playlist != null){
                    _state.postValue(PlaylistsDetailsState.Result(playlist))
                } else {
                    _state.postValue(PlaylistsDetailsState.Error)
                }
            } }
    }
}