package com.practicum.playlistmaker.media_library.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addTrackToFavoriteList(track: Track)
    suspend fun removeTrackFromFavoriteList(track: Track)
    fun getAllFavoriteTracks(): Flow<List<Track>>
}