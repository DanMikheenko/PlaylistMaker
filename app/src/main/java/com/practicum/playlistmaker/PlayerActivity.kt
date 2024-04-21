package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class PlayerActivity() : AppCompatActivity() {

    lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val intent = intent
        var selectedTrackJson = intent.getStringExtra("selectedTrack")
        track = Gson().fromJson<Track>(selectedTrackJson, Track::class.java)

        val trackImagePlayer: ImageView = findViewById(R.id.trackImagePlayer)
        Glide.with(trackImagePlayer)
            .load(track.getCoverArtwork())
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.player_image_placeholder)
            .error(R.drawable.player_image_placeholder)
            .into(trackImagePlayer)


        val trackName = findViewById<TextView>(R.id.trackNamePlayer)
        trackName.text = track.trackName

        val trackAuthor = findViewById<TextView>(R.id.trackAuthor)
        trackAuthor.text = track.artistName

        val trackDurationTextView = findViewById<TextView>(R.id.trackDurationTextView)
        trackDurationTextView.text = track.formattedDuration()

        val albNameTextView = findViewById<TextView>(R.id.albNameTextView)
        albNameTextView.text = track.collectionName

        val yearTextView = findViewById<TextView>(R.id.yearTextView)
        yearTextView.text = track.releaseDate

        val genreTextView = findViewById<TextView>(R.id.genreTextView)
        genreTextView.text = track.primaryGenreName

        val countryTextView = findViewById<TextView>(R.id.countryTextView)
        countryTextView.text = track.country


        val playerBackBtn = findViewById<TextView>(R.id.playerBackButton)
        playerBackBtn.setOnClickListener {
            finish()
        }
    }


}