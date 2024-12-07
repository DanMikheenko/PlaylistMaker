package com.practicum.playlistmaker.search.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class TrackEntity (
    @PrimaryKey
    val id: String,
    var trackName: String,
    var previewUrl: String?,
    var artistName: String,
    var trackTimeMillis: String,
    var artworkUrl100: String,
    var collectionName: String,
    var releaseDate: String?,
    var primaryGenreName: String,
    var country: String
)

