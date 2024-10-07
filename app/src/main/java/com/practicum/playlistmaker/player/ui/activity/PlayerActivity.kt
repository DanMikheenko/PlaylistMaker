package com.practicum.playlistmaker.player.ui.activity

import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModelFactory
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track
    private lateinit var playButton: TextView
    private lateinit var secondsLeftTextView: TextView
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent
        val selectedTrackJson = intent.getStringExtra(SELECTED_TRACK)
        track = Gson().fromJson(selectedTrackJson, Track::class.java)

        val factory =
            PlayerViewModelFactory(Creator.providePlayerInteractor(), track)
        viewModel = ViewModelProvider(this, factory).get(PlayerViewModel::class.java)
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
        viewModel.preparePlayer(
            onPrepared = { playButton.isEnabled = true },
            onCompletion = {
                runnable?.let { mainThreadHandler?.removeCallbacks(it) }
                secondsLeftTextView.text = "00:00"
                updatePlayButtonBackground()
            })
    }

    private fun startPlayer() {
        viewModel.startPlayer()
        updatePauseButtonBackground()

        runnable = object : Runnable {
            override fun run() {
                secondsLeftTextView.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(viewModel.getPlayerCurrentPosition())
                mainThreadHandler?.postDelayed(this, 10)
            }
        }
        mainThreadHandler?.post(runnable!!)
    }

    private fun pausePlayer() {
        viewModel.pausePlayer()
        updatePlayButtonBackground()
    }

    private fun playbackControl() {
        var state : PlayerState
        state = PlayerState.Default
        viewModel.state.observe(this) { _state ->
            state = _state
        }
        when (state) {
            PlayerState.Playing -> pausePlayer()
            PlayerState.Prepared, PlayerState.Paused -> startPlayer()
            PlayerState.Default -> startPlayer()
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
        viewModel.releasePlayer()
        runnable?.let { mainThreadHandler?.removeCallbacks(it) }
    }

    companion object {
        private const val SELECTED_TRACK = "selectedTrack"
    }
}