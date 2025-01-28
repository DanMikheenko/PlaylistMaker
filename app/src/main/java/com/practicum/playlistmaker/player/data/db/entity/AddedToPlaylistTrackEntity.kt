package com.practicum.playlistmaker.player.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "added_to_playlist_tracks_table")
data class AddedToPlaylistTrackEntity(
    @PrimaryKey
    val trackId: String,
    var trackName: String,
    var previewUrl: String?,
    var artistName: String,
    var trackTimeMillis: String,
    var artworkUrl100: String,
    var collectionName: String?,
    var releaseDate: String?,
    var primaryGenreName: String?,
    var country: String?
)
