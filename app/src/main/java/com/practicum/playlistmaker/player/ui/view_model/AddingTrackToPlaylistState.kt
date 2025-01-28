package com.practicum.playlistmaker.player.ui.view_model

interface AddingTrackToPlaylistState {
    data object PlaylistContainsTrack: AddingTrackToPlaylistState
    data object TrackAddedToPlaylist: AddingTrackToPlaylistState
}