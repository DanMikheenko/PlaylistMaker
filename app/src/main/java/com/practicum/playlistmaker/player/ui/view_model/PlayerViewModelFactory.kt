package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModelFactory(
    private val playerInteractor: PlayerInteractor,
    private val track: Track
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(playerInteractor, track) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}