package com.practicum.playlistmaker.domain.models

data class Track(
    var trackId: String,
    var trackName: String,
    var previewUrl: String?,
    var artistName: String,
    var trackTimeMillis: String,
    var artworkUrl100: String,
    var collectionName: String,
    var releaseDate: String?,
    var primaryGenreName: String,
    var country: String
) {
    fun formattedDuration(): String {
        val totalSeconds = trackTimeMillis.toLong() / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
