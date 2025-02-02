package com.practicum.playlistmaker.media_library.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_library.domain.models.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistImage : ImageView = itemView.findViewById(R.id.playlistImageView)
    private val playlistName : TextView = itemView.findViewById(R.id.playlistName)
    private val tracksCount : TextView = itemView.findViewById(R.id.tracksCount)

    fun bind(model: Playlist){
        Glide.with(playlistImage)
            .load(model.playlistImagePath)
            //.centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.track_placeholder_image)
            .error(R.drawable.track_placeholder_image)
            .into(playlistImage)
        playlistName.text = model.playlistName
        tracksCount.text = getTracksCountFormatted(model.addedTracksCount.toInt())
    }
    private fun getTracksCountFormatted(counts: Int): String {
        return when {
            counts % 10 == 1 && counts % 100 != 11 -> "$counts трек"
            counts % 10 in 2..4 && counts % 100 !in 12..14 -> "$counts трека"
            else -> "$counts треков"
        }
    }
}