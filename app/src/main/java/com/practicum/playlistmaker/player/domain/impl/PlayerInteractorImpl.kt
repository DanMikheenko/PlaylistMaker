package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun preparePlayer(
        track: Track,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        playerRepository.preparePlayer(track, onPrepared, onCompletion)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }
}