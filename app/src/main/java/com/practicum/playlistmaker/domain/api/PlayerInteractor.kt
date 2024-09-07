package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer(
        track: Track,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
    }

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean
}