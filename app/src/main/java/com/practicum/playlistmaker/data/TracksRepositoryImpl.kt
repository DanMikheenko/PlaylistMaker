package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.previewUrl,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country
                )
            }
        } else {
            return emptyList()
        }
    }
}