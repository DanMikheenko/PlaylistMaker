package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val track: Track,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) :
    ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = _state

    private val _playingTrackPosition = MutableLiveData<Int>()
    val playingTrackPosition: LiveData<Int> = _playingTrackPosition

    private val _isFavoriteTrack = MutableLiveData<Boolean>()
    val isFavoriteTrack : LiveData<Boolean> = _isFavoriteTrack

    private var playbackJob: Job? = null

    init {
        _state.postValue(PlayerState.Default)
    }

    fun preparePlayer(
    ) {
        playerInteractor.preparePlayer(track)
        playerInteractor.setOnPrepared {
            _state.postValue(PlayerState.Prepared)
        }
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        _state.postValue(PlayerState.Playing)
        startTrackingPlayingTrackPosition()
        playerInteractor.setOnCompletion {
            stopTrackingPlayingTrackPosition()
            _playingTrackPosition.postValue(0)
            _state.postValue(PlayerState.Default)
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _state.postValue(PlayerState.Paused)
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        _state.postValue(PlayerState.Default)
        stopTrackingPlayingTrackPosition()
    }

    fun getPlayerCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun startTrackingPlayingTrackPosition() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(CLICK_DEBOUNCE_DELAY)
                _playingTrackPosition.value = getPlayerCurrentPosition()
            }
        }
    }

    fun stopTrackingPlayingTrackPosition() {
        playbackJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopTrackingPlayingTrackPosition()
    }

    fun onFavoriteClicked(){
        if (track.isFavorite) {
            viewModelScope.launch {
                favoriteTracksInteractor.removeTrackFromFavoriteList(track)
                track.isFavorite = false
                _isFavoriteTrack.postValue(false)
            }
        } else{
            viewModelScope.launch {
                favoriteTracksInteractor.addTrackToFavoriteList(track)
                track.isFavorite = true
                _isFavoriteTrack.postValue(true)
            }
        }
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }
}