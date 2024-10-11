package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}