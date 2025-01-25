package com.practicum.playlistmaker.media_library.domain.api

import com.practicum.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun add(playlist: Playlist)
    suspend fun remove(playlist: Playlist)
    suspend fun getAll(): Flow<List<Playlist>?>
    suspend fun getPlaylistById(id: Int): Flow<Playlist?>
}