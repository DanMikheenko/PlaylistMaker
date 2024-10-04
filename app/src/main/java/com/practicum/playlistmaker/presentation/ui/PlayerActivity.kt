package com.practicum.playlistmaker.presentation.ui

import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var playerState = STATE_DEFAULT
    private lateinit var interactor: PlayerInteractor
    private lateinit var track: Track
    private lateinit var playButton: TextView
    private lateinit var secondsLeftTextView: TextView
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        interactor = Creator.providePlayerInteractor()

        val intent = intent
        val selectedTrackJson = intent.getStringExtra(SELECTED_TRACK)
        track = Gson().fromJson(selectedTrackJson, Track::class.java)

        playButton = findViewById(R.id.playButton)
        secondsLeftTextView = findViewById(R.id.trackTimeMillisTextView)

        preparePlayer()

        playButton.setOnClickListener {
            playbackControl()
        }

        setupUI()
    }

    private fun setupUI() {
        val trackImagePlayer: ImageView = findViewById(R.id.trackImagePlayer)
        Glide.with(trackImagePlayer)
            .load(track.getCoverArtwork())
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.player_image_placeholder)
            .error(R.drawable.player_image_placeholder)
            .into(trackImagePlayer)

        findViewById<TextView>(R.id.trackNamePlayer).text = track.trackName
        findViewById<TextView>(R.id.trackAuthor).text = track.artistName
        findViewById<TextView>(R.id.trackDurationTextView).text = track.formattedDuration()
        findViewById<TextView>(R.id.albNameTextView).text = track.collectionName
        findViewById<TextView>(R.id.yearTextView).text = track.releaseDate?.substring(0, 4)
        findViewById<TextView>(R.id.genreTextView).text = track.primaryGenreName
        findViewById<TextView>(R.id.countryTextView).text = track.country

        findViewById<TextView>(R.id.playerBackButton).setOnClickListener {
            finish()
        }
    }

    private fun preparePlayer() {
        interactor.preparePlayer(
            track,
            onPrepared = {
                playButton.isEnabled = true
            },
            onCompletion = {
                playerState = STATE_PREPARED
                runnable?.let { mainThreadHandler?.removeCallbacks(it) }
                secondsLeftTextView.text = "00:00"
                updatePlayButtonBackground()
            }
        )
    }

    private fun startPlayer() {
        interactor.startPlayer()
        updatePauseButtonBackground()

        runnable = object : Runnable {
            override fun run() {
                secondsLeftTextView.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(interactor.getCurrentPosition())
                mainThreadHandler?.postDelayed(this, 10)
            }
        }
        mainThreadHandler?.post(runnable!!)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        updatePlayButtonBackground()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
            STATE_DEFAULT -> startPlayer()
        }
    }

    private fun updatePlayButtonBackground() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                playButton.setBackgroundResource(R.drawable.play_button_dark)
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                playButton.setBackgroundResource(R.drawable.play_button)
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                playButton.setBackgroundResource(R.drawable.play_button)
            }
        }
    }

    private fun updatePauseButtonBackground() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                playButton.setBackgroundResource(R.drawable.pause_button_image_dark_theme)
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                playButton.setBackgroundResource(R.drawable.pause_button_image_light_theme)
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                playButton.setBackgroundResource(R.drawable.pause_button_image_light_theme)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.releasePlayer()
        runnable?.let { mainThreadHandler?.removeCallbacks(it) }
    }

    companion object {
        private const val SELECTED_TRACK = "selectedTrack"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}