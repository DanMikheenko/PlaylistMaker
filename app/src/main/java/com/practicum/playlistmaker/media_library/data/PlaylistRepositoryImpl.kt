package com.practicum.playlistmaker.media_library.data

import com.practicum.playlistmaker.media_library.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media_library.domain.models.Playlist
import com.practicum.playlistmaker.search.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistRepository {
    override suspend fun add(playlist: Playlist) {
        appDatabase.playlistDao().insert(playlistDbConvertor.map(playlist))
    }

    override suspend fun remove(playlist: Playlist) {
        appDatabase.playlistDao().delete(playlistDbConvertor.map(playlist))
    }

    override suspend fun getAll(): Flow<List<Playlist>?> = flow {
        appDatabase.playlistDao().getAllPlaylists().collect(){playlists->
            if (playlists.isNullOrEmpty()){
                emit(emptyList())
            } else{
                emit(convert(playlists))
            }
        }
    }

    override suspend fun getPlaylistById(id: String): Flow<Playlist?> = flow {
        appDatabase.playlistDao().getPlaylistById(id).collect(){playlist->
            if (playlist == null) {
                emit(null)
            } else{
                emit(playlistDbConvertor.map(playlist))
            }
        }
    }

    private fun convert(playlistsEntity: List<PlaylistEntity>): List<Playlist>{
        return playlistsEntity.map{ playlist -> playlistDbConvertor.map(playlist)}
    }
}