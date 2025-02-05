package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class PlaylistCreationViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    fun createPlaylist(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.add(playlist)
        }
    }
}