package com.practicum.playlistmaker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_library.domain.models.Playlist

class PlaylistPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistImage : ImageView = itemView.findViewById(R.id.playlistImageViewPlayer)
    private val playlistName : TextView = itemView.findViewById(R.id.playlistNamePlayer)
    private val tracksCount : TextView = itemView.findViewById(R.id.tracksCountPlayer)

    fun bind(model: Playlist){
        Glide.with(playlistImage)
            .load(model.playlistImagePath)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.track_placeholder_image)
            .error(R.drawable.track_placeholder_image)
            .into(playlistImage)
        playlistName.text = model.playlistName
        tracksCount.text = getCorrectCountName(model.addedTracksCount)
    }

    private fun getCorrectCountName(count: String): String{
        if (count.toInt()==0){
            return "Плейлист пуст"
        }
        if (count.toInt()<5){
            return count+ " трека"
        }else return count+ " треков"
    }
}