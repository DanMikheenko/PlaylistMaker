package com.practicum.playlistmaker.player.ui.view_model

import com.practicum.playlistmaker.media_library.domain.models.Playlist

interface PlaylistPlayerState {
    data object ShowNothing : PlaylistPlayerState
    data class ShowResult(val data: List<Playlist>): PlaylistPlayerState

}