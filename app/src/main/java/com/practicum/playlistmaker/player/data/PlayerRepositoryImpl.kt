package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun setOnPrepared(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
    }
    override fun setOnCompletion(onCompletion: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }
}