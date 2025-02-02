package com.practicum.playlistmaker.media_library.domain.impl

import com.practicum.playlistmaker.media_library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun add(playlist: Playlist) {
        playlistRepository.add(playlist)
    }

    override suspend fun remove(playlist: Playlist) {
        playlistRepository.remove(playlist)
    }

    override suspend fun getAll(): Flow<List<Playlist>?> {
        return playlistRepository.getAll()
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist?> {
        return playlistRepository.getPlaylistById(id)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun getTrackById(trackId: Int): Flow<Track?> {
        return playlistRepository.getTrackById(trackId)
    }
}