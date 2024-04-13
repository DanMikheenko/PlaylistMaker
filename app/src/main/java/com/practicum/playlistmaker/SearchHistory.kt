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

    private fun readSearchHistory(): LinkedList<Track> {
        val jsonTracks = sharedPreferences
            .getString(SEARCH_HISTORY, "")
        if (jsonTracks.isNullOrEmpty()) return LinkedList<Track>()
        return Gson().fromJson(jsonTracks, Array<Track>::class.java).toCollection(LinkedList())

    }

    fun addNewTrackToHistory(track: Track) {
        for (item in readSearchHistory()) {
            if (item.trackId == track.trackId) {
                readSearchHistory().remove(item)
                readSearchHistory().addFirst(track)
            } else {
                readSearchHistory().add(track)
            }

            if (readSearchHistory().count() > 10) {
                readSearchHistory().removeLast()
            }
        }
        saveTracksToHistory(readSearchHistory())
    }

    fun clear() {
        saveTracksToHistory(LinkedList<Track>())
    }

}