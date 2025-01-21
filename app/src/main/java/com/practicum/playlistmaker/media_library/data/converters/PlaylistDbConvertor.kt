package com.practicum.playlistmaker.media_library.data.converters

import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.domain.models.Playlist

class PlaylistDbConvertor {
    fun map(playlistEntity: PlaylistEntity): Playlist{
        return Playlist(
            playlistId = playlistEntity.playlistId,
            playlistName = playlistEntity.playlistName,
            description = playlistEntity.description,
            playlistImagePath = playlistEntity.playlistImagePath,
            addedTracksCount = playlistEntity.addedTracksCount,
            addedTracksId = playlistEntity.addedTracksId
        )
    }
    fun map(playlist: Playlist): PlaylistEntity{
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            description = playlist.description,
            playlistImagePath = playlist.playlistImagePath,
            addedTracksCount = playlist.addedTracksCount,
            addedTracksId = playlist.addedTracksId)
    }
}