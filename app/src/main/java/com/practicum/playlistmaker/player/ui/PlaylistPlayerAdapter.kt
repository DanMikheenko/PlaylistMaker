package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_library.domain.models.Playlist

class PlaylistPlayerAdapter(private val playlists: List<Playlist>): RecyclerView.Adapter<PlaylistPlayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistPlayerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_view_player, parent, false)
        return PlaylistPlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistPlayerViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}