package com.practicum.playlistmaker.sharing.data

data class EmailData(
    val destinationEmailAddress : String,
    val message: String,
    val extraSubject: String
)
