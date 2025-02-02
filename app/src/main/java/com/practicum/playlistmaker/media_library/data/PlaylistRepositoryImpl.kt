package com.practicum.playlistmaker.media_library.data

import com.practicum.playlistmaker.media_library.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.player.data.db.entity.AddedToPlaylistTrackEntity
import com.practicum.playlistmaker.search.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.search.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor
) : PlaylistRepository {
    override suspend fun add(playlist: Playlist) {
        appDatabase.playlistDao().insert(playlistDbConvertor.map(playlist))
    }

    override suspend fun remove(playlist: Playlist) {
        appDatabase.playlistDao().delete(playlistDbConvertor.map(playlist))
    }

    override suspend fun getAll(): Flow<List<Playlist>?> = flow {
        appDatabase.playlistDao().getAllPlaylists().collect() { playlists ->
            if (playlists.isNullOrEmpty()) {
                emit(emptyList())
            } else {
                emit(convert(playlists))
            }
        }
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist?> = flow {
        appDatabase.playlistDao().getPlaylistById(id).collect() { playlist ->
            if (playlist == null) {
                emit(null)
            } else {
                emit(playlistDbConvertor.map(playlist))
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        appDatabase.addedToPlaylistTrackDao().insert(
            AddedToPlaylistTrackEntity(
                track.trackId,
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
        )

        var tracksIds = ""
        var tracksCount = playlist.addedTracksCount.toInt()
        if (playlist.addedTracksId.isNullOrEmpty()) {
            tracksIds = track.trackId
            tracksCount++
        } else {
            tracksIds = playlist.addedTracksId + " " + track.trackId
            tracksCount++
        }

        appDatabase.playlistDao().insert(
            PlaylistEntity(
                playlist.playlistId,
                playlist.playlistName,
                playlist.description,
                playlist.playlistImagePath,
                tracksIds,
                tracksCount.toString()
            )
        )
    }

    override suspend fun getTrackById(trackId: Int): Flow<Track?> = flow {
        appDatabase.addedToPlaylistTrackDao().getTrackById(trackId).collect() { track ->
            if (track == null) {
                emit(null)
            } else {
                emit(trackDbConvertor.map(track))
            }
        }
    }

    private fun convert(playlistsEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistsEntity.map { playlist -> playlistDbConvertor.map(playlist) }
    }
}