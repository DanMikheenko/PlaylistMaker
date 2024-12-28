package com.practicum.playlistmaker.search.data.dto

data class TrackDto(
    val trackId: String,
    val trackName: String,
    val previewUrl: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)