package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel : ViewModel() {
    private val _state = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState> get() = _state

    fun loadData(){
        _state.postValue(FavoriteTracksState.ShowPlaceholder)
    }
}