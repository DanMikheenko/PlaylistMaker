package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun setOnPrepared(onPrepared: () -> Unit)
    fun setOnCompletion(onCompletion: () -> Unit)
}