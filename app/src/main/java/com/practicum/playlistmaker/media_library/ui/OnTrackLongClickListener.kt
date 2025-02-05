package com.practicum.playlistmaker.media_library.ui

import com.practicum.playlistmaker.search.domain.models.Track

interface OnTrackLongClickListener {
    fun onTrackLongClick(track: Track)
}