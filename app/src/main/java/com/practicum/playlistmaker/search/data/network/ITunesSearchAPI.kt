package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {
    @GET("/search?entity=song ")
    suspend fun search(
        @Query("term") term: String
    ): TracksSearchResponse

}