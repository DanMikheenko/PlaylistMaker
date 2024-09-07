package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}