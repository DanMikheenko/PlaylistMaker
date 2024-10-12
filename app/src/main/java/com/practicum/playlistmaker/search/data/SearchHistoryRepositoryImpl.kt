package com.practicum.playlistmaker.search.data

import com.google.gson.Gson
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.LinkedList

class SearchHistoryRepositoryImpl() :
    SearchHistoryRepository {
    companion object {
        private const val SEARCH_HISTORY = "searchHistory"
        private const val HISTORY_CAPACITY = 10
    }

    private fun saveTracksToHistory(tracks: LinkedList<Track>) {
        val gson = Gson()
        val jsonTracks = gson.toJson(tracks)
        Creator.getSharedPreferences().edit()
            .putString(SEARCH_HISTORY, jsonTracks)
            .apply()
    }

    override fun readSearchHistory(): List<Track> {
        val jsonTracks = Creator.getSharedPreferences()
            .getString(SEARCH_HISTORY, "")
        if (jsonTracks.isNullOrEmpty()) return LinkedList<Track>()
        return Gson().fromJson(jsonTracks, Array<Track>::class.java).toCollection(LinkedList())

    }

    override fun addNewTrackToHistory(track: Track) {
        val storedTracks = readSearchHistory()
        if (storedTracks.isEmpty()) {
            val tracks = LinkedList<Track>()
            tracks.addFirst(track)
            saveTracksToHistory(tracks)
        } else {
            val newLinkedList = LinkedList<Track>()
            for (item in storedTracks) {
                if (item.trackId != track.trackId) {
                    newLinkedList.addLast(item)
                }
            }
            newLinkedList.addFirst(track)
            if (newLinkedList.count() > HISTORY_CAPACITY) {
                newLinkedList.removeLast()
            }
            saveTracksToHistory(newLinkedList)
        }

    }

    override fun clear() {
        saveTracksToHistory(LinkedList<Track>())
    }
}