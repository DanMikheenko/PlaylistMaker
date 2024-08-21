package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(
    private val tracks: List<Track>,
    private val sharedPreferences: SharedPreferences,
) : RecyclerView.Adapter<TrackViewHolder>() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(tracks[position])
        val searchHistory = SearchHistory(sharedPreferences)
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                searchHistory.addNewTrackToHistory(tracks[position])
                val playerIntent = Intent(holder.itemView.context, PlayerActivity::class.java)
                playerIntent.putExtra("selectedTrack", Gson().toJson(tracks[position]))
                holder.itemView.context.startActivity(playerIntent)
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}