package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.LinkedList

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) :
    SearchHistoryRepository {
    companion object {
        private const val SEARCH_HISTORY = "searchHistory"
        private const val HISTORY_CAPACITY = 10
    }

    private fun saveTracksToHistory(tracks: LinkedList<Track>) {
        val jsonTracks = gson.toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, jsonTracks)
            .apply()
    }

    override suspend fun readSearchHistory(): List<Track> {
        val favoriteTracksIds = appDatabase.trackDao().getFavoriteTracksIds()
        val jsonTracks = sharedPreferences
            .getString(SEARCH_HISTORY, "")
        if (jsonTracks.isNullOrEmpty()) return LinkedList<Track>()
        val tracks = Gson().fromJson(jsonTracks, Array<Track>::class.java).toCollection(LinkedList())
        return tracks.map { track ->
            track.copy(isFavorite = favoriteTracksIds.contains(track.id))
        }

    }

    override suspend fun addNewTrackToHistory(track: Track) {
        val storedTracks = readSearchHistory()
        if (storedTracks.isEmpty()) {
            val tracks = LinkedList<Track>()
            tracks.addFirst(track)
            saveTracksToHistory(tracks)
        } else {
            val newLinkedList = LinkedList<Track>()
            for (item in storedTracks) {
                if (item.id != track.id) {
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