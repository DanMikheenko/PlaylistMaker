package com.practicum.playlistmaker.media_library.domain.models

data class Playlist(
    val playlistId: Int,
    var playlistName: String,
    var description: String,
    var playlistImagePath: String,
    var addedTracksId: String,
    var addedTracksCount: String
)
