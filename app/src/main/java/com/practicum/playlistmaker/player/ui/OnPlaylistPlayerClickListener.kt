package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.media_library.domain.models.Playlist

interface OnPlaylistPlayerClickListener {
    fun onPlaylistPlayerClick(playlist: Playlist)
}