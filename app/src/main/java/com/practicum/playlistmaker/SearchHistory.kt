package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.LinkedList

class SearchHistory(val sharedPreferences: SharedPreferences) {
    companion object {
        const val SEARCH_HISTORY = "searchHistory"
    }

    private fun saveTracksToHistory(tracks: LinkedList<Track>) {
        val jsonTracks = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, jsonTracks)
            .apply()
    }

    fun readSearchHistory(): LinkedList<Track> {
        val jsonTracks = sharedPreferences
            .getString(SEARCH_HISTORY, "")
        if (jsonTracks.isNullOrEmpty()) return LinkedList<Track>()
        return Gson().fromJson(jsonTracks, Array<Track>::class.java).toCollection(LinkedList())

    }

    fun addNewTrackToHistory(track: Track, tracks: LinkedList<Track>) {
        for (item in tracks) {
            if (item.trackId == track.trackId) {
                tracks.remove(item)
                tracks.addFirst(track)
            } else {
                tracks.add(track)
            }

            if (tracks.count() > 10) {
                tracks.removeLast()
            }
        }
        saveTracksToHistory(tracks)
    }

    fun clear() {
        saveTracksToHistory(LinkedList<Track>())
    }

}