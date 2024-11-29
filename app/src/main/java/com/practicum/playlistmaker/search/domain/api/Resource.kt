package com.practicum.playlistmaker.search.domain.api

sealed class Resource <T>(val data: T? = null, val errorType: ErrorTypes? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(errorType: ErrorTypes, data: T? = null): Resource<T>(data, errorType)
}