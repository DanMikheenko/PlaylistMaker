package com.practicum.playlistmaker

import android.content.res.Configuration
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    companion object {
        private const val SELECTED_TRACK = "selectedTrack"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
    private lateinit var track: Track
    private var mediaPlayer = MediaPlayer()
    private lateinit var play: TextView
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private lateinit var secondsLeftTextView: TextView
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK
            window.navigationBarColor = Color.BLACK
        }

        val intent = intent
        val selectedTrackJson = intent.getStringExtra(SELECTED_TRACK)
        track = Gson().fromJson<Track>(selectedTrackJson, Track::class.java)
        play = findViewById(R.id.playButton)
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
        secondsLeftTextView = findViewById(R.id.trackTimeMillisTextView)

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
        yearTextView.text = track.releaseDate.substring(0, 4)

        val genreTextView = findViewById<TextView>(R.id.genreTextView)
        genreTextView.text = track.primaryGenreName

        val countryTextView = findViewById<TextView>(R.id.countryTextView)
        countryTextView.text = track.country


        val playerBackBtn = findViewById<TextView>(R.id.playerBackButton)
        playerBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            runnable?.let { mainThreadHandler?.removeCallbacks(it) }
            secondsLeftTextView.text = "00:00"
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    play.setBackgroundResource(R.drawable.play_button_dark)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    play.setBackgroundResource(R.drawable.play_button)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    play.setBackgroundResource(R.drawable.play_button)
                }
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()

        // Проверка текущей темы
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                play.setBackgroundResource(R.drawable.pause_button_image_dark_theme)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                play.setBackgroundResource(R.drawable.pause_button_image_light_theme)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                play.setBackgroundResource(R.drawable.pause_button_image_light_theme)
            }
        }

        runnable = object : Runnable {
            override fun run() {
                secondsLeftTextView.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, 10)
            }
        }
        mainThreadHandler?.post(runnable!!)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                play.setBackgroundResource(R.drawable.play_button_dark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                play.setBackgroundResource(R.drawable.play_button)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                play.setBackgroundResource(R.drawable.play_button)
            }
        }
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        runnable?.let { mainThreadHandler?.removeCallbacks(it) }
    }
}