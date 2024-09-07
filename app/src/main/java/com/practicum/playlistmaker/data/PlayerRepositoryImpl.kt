package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.PlayerRepository
import com.practicum.playlistmaker.domain.models.Track

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}