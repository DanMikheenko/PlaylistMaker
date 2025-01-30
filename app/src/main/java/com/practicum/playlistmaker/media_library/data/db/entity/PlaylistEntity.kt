package com.practicum.playlistmaker.media_library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    var playlistName: String,
    var description: String,
    var playlistImagePath: String,
    var addedTracksId: String,
    var addedTracksCount: String
)