package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.api.ErrorTypes
import com.practicum.playlistmaker.search.domain.api.Resource
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ErrorTypes.InternetConnectionError))
            }
            200 -> {
                with(response as TracksSearchResponse){
                    val data = results.map {
                        Track(
                            it.id,
                            it.trackName,
                            it.previewUrl,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country)
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