package com.practicum.playlistmaker.search.domain.api

interface ErrorTypes {
    data object ServerError : ErrorTypes
    data object InternetConnectionError : ErrorTypes
}