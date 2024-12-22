package com.practicum.playlistmaker.media_library.data

import com.practicum.playlistmaker.media_library.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.search.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.search.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTracksRepository {
    override suspend fun addTrackToFavoriteList(track: Track) {
        appDatabase.trackDao().insert(trackDbConvertor.map(trackDbConvertor.map(track)))
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        appDatabase.trackDao().delete(trackDbConvertor.map(trackDbConvertor.map(track)))
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromMovieEntity(tracks))
    }

    private fun convertFromMovieEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}