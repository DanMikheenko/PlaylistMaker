package com.practicum.playlistmaker.search.data.converters

import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.models.Track

class TrackDbConvertor {
    fun map(track: Track): TrackDto {
        return TrackDto(
            track.id,
            track.trackName,
            track.previewUrl.toString(),
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate.toString(),
            track.primaryGenreName,
            track.country
        )
    }

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.id,
            track.trackName,
            track.previewUrl,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.id,
            track.trackName,
            track.previewUrl,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }
}