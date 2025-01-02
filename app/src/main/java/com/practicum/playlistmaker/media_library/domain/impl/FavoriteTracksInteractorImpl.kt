package com.practicum.playlistmaker.media_library.domain.impl

import com.practicum.playlistmaker.media_library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.media_library.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun addTrackToFavoriteList(track: Track) {
        favoriteTracksRepository.addTrackToFavoriteList(track)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        favoriteTracksRepository.removeTrackFromFavoriteList(track)
    }

    override suspend fun getAllFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getAllFavoriteTracks()
    }

    override suspend fun getAllFavoriteTracksIds(): Flow<List<String>> {
        return favoriteTracksRepository.getAllFavoriteTracksIds()
    }
}