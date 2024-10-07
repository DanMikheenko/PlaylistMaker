package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val track: Track
) :
    ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    init {
        _state.postValue(PlayerState.Default)
    }

    fun preparePlayer(
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        playerInteractor.preparePlayer(track, onPrepared, onCompletion)
        _state.postValue(PlayerState.Prepared)
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        _state.postValue(PlayerState.Playing)
    }

    fun pausePlayer(){
        playerInteractor.pausePlayer()
        _state.postValue(PlayerState.Paused)
    }

    fun releasePlayer(){
        playerInteractor.releasePlayer()
        _state.postValue(PlayerState.Default)
    }
    fun getPlayerCurrentPosition() : Int{
        return playerInteractor.getCurrentPosition()
    }
}