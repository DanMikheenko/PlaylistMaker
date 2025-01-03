package com.practicum.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var playButton: TextView
    private lateinit var secondsLeftTextView: TextView
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private lateinit var playerState: PlayerState

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent
        val selectedTrackJson = intent.getStringExtra(SELECTED_TRACK)
        track = Gson().fromJson(selectedTrackJson, Track::class.java)

        playButton = findViewById(R.id.playButton)
        secondsLeftTextView = findViewById(R.id.trackTimeMillisTextView)

        preparePlayer()

        playButton.setOnClickListener {
            playClick()
        }

        setupUI()
        val likeButton = findViewById<ImageView>(R.id.like_button)
        viewModel.isFavoriteTrack.observe(this){_isFavorite->
            if (_isFavorite){
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        likeButton.setBackgroundResource(R.drawable.button_2)
                    }

                    Configuration.UI_MODE_NIGHT_NO -> {
                        likeButton.setBackgroundResource(R.drawable.liked_button)
                    }

                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        playButton.setBackgroundResource(R.drawable.liked_button)
                    }
                }
            } else{
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        likeButton.setBackgroundResource(R.drawable.like_button)
                    }

                    Configuration.UI_MODE_NIGHT_NO -> {
                        likeButton.setBackgroundResource(R.drawable.like_button)
                    }

                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        playButton.setBackgroundResource(R.drawable.like_button)
                    }
                }
            }
        }
        likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.state.observe(this) { _state ->
            playerState = _state
            render()
        }

        viewModel.playingTrackPosition.observe(this) { _seconds ->
            secondsLeftTextView.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(_seconds)
        }
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
        viewModel.preparePlayer()
    }

    private fun startPlayer() {
        viewModel.startPlayer()
        updatePauseButtonBackground()
    }

    private fun pausePlayer() {
        viewModel.pausePlayer()
        updatePlayButtonBackground()
    }

    private fun playClick() {
        when (playerState) {
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
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val SELECTED_TRACK = "selectedTrack"
    }

    private fun render() {
        when (playerState) {
            PlayerState.Playing -> updatePauseButtonBackground()
            PlayerState.Prepared, PlayerState.Paused -> {
                updatePlayButtonBackground()
                playButton.isEnabled = true
            }
            PlayerState.Default -> {
                updatePlayButtonBackground()
                secondsLeftTextView.text = "00:00"
                updatePlayButtonBackground()
                runnable?.let { mainThreadHandler?.removeCallbacks(it) }
            }
        }
    }
}