package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.ErrorTypes
import com.practicum.playlistmaker.search.domain.api.Resource
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val appDatabase: AppDatabase) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        var favoriteTracksIds = emptyList<String>()
//        appDatabase.trackDao().getFavoriteTracksIds().collect(){ids->
//            if (ids.isNullOrEmpty()){
//                return@collect
//            } else{
//                favoriteTracksIds = ids
//            }
//        }
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ErrorTypes.InternetConnectionError))
            }
            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map { result ->
                        Track(
                            trackId = result.trackId,
                            trackName = result.trackName,
                            previewUrl = result.previewUrl,
                            artistName = result.artistName,
                            trackTimeMillis = result.trackTimeMillis,
                            artworkUrl100 = result.artworkUrl100,
                            collectionName = result.collectionName,
                            releaseDate = result.releaseDate,
                            primaryGenreName = result.primaryGenreName,
                            country = result.country,
                            isFavorite = favoriteTracksIds.contains(result.trackId)
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(ErrorTypes.ServerError))
            }
        }
    }
}