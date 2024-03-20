package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {
        trackName.text = model.trackName
        Glide.with(trackImage).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(10))
            .placeholder(R.drawable.ic_track_image_placeholder_background).into(trackImage)
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }

}