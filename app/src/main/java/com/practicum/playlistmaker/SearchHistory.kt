package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.LinkedList

class SearchHistory(val sharedPreferences: SharedPreferences) {
    companion object {
        private const val SEARCH_HISTORY = "searchHistory"
    }

    private fun saveTracksToHistory(tracks: LinkedList<Track>) {
        val gson = Gson()
        val jsonTracks = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, jsonTracks)
            .apply()
    }

    fun readSearchHistory(): List<Track> {
        val jsonTracks = sharedPreferences
            .getString(SEARCH_HISTORY, "")
        if (jsonTracks.isNullOrEmpty()) return LinkedList<Track>()
        return Gson().fromJson(jsonTracks, Array<Track>::class.java).toCollection(LinkedList())

    }

    fun addNewTrackToHistory(track: Track) {
        val storedTracks = readSearchHistory()
        if (storedTracks.isEmpty()){
            val tracks = LinkedList<Track>()
            tracks.addFirst(track)
            saveTracksToHistory(tracks)
        }else{
            val newLinkedList = LinkedList<Track>()
            for (item in storedTracks) {
                if (item.trackId != track.trackId) {
                    newLinkedList.addFirst(item)
                }
            }
            newLinkedList.addFirst(track)
            if (newLinkedList.count() > 10) {
                newLinkedList.removeLast()
            }
            saveTracksToHistory(newLinkedList)

        }

    }

    fun clear() {
        saveTracksToHistory(LinkedList<Track>())
    }

}