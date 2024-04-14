package com.practicum.playlistmaker

data class Track(
    var trackId: String,
    var trackName: String,
    var artistName: String,
    var trackTimeMillis: String,
    var artworkUrl100: String
) {
    fun formattedDuration(): String {
        val totalSeconds = trackTimeMillis.toLong() / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
