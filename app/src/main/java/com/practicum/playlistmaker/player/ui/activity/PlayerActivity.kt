package com.practicum.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.PlaylistPlayerAdapter
import com.practicum.playlistmaker.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlaylistPlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

class PlayerFragment : Fragment(R.layout.fragment_player) {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedTrackJson = arguments?.getString(SELECTED_TRACK)
        track = Gson().fromJson(selectedTrackJson, Track::class.java)

        playButton = view.findViewById(R.id.playButton)
        secondsLeftTextView = view.findViewById(R.id.trackTimeMillisTextView)

        preparePlayer()

        playButton.setOnClickListener {
            playClick()
        }

        setupUI()
        val likeButton = view.findViewById<ImageView>(R.id.like_button)
        viewModel.isFavoriteTrack.observe(viewLifecycleOwner) { _isFavorite ->
            if (_isFavorite) {
                updateLikeButtonForFavorite(true, likeButton)
            } else {
                updateLikeButtonForFavorite(false, likeButton)
            }
        }

        likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.state.observe(viewLifecycleOwner) { _state ->
            playerState = _state
            render()
        }

        viewModel.playingTrackPosition.observe(viewLifecycleOwner) { _seconds ->
            secondsLeftTextView.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(_seconds)
        }

        val bottomSheetContainer = view.findViewById<LinearLayout>(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val addTrackToPlaylist = view.findViewById<ImageView>(R.id.addTrackToPlaylistButton)
        addTrackToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            val backgroundView = view.findViewById<View>(R.id.scroll)
            backgroundView.alpha = 0.1f

            viewModel.loadPlaylists()
            viewModel.playlistsState.observe(viewLifecycleOwner) { _state ->
                showPlaylists(_state)
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val backgroundView = view.findViewById<View>(R.id.scroll)
                backgroundView.alpha = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) 0.1f else 1f
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun updateLikeButtonForFavorite(isFavorite: Boolean, likeButton: ImageView) {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                likeButton.setBackgroundResource(
                    if (isFavorite) R.drawable.button_2 else R.drawable.like_button
                )
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                likeButton.setBackgroundResource(
                    if (isFavorite) R.drawable.liked_button else R.drawable.like_button
                )
            }
            else -> {
                likeButton.setBackgroundResource(
                    if (isFavorite) R.drawable.liked_button else R.drawable.like_button
                )
            }
        }
    }

    fun showPlaylists(state: PlaylistPlayerState) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewPlaylistPlayer)
        recyclerView?.visibility = if (state is PlaylistPlayerState.ShowNothing) View.GONE else View.VISIBLE
        if (state is PlaylistPlayerState.ShowResult) {
            recyclerView?.adapter = PlaylistPlayerAdapter(state.data)
        }
    }

    private fun setupUI() {
        val trackImagePlayer: ImageView = view?.findViewById(R.id.trackImagePlayer)!!
        Glide.with(trackImagePlayer)
            .load(track.getCoverArtwork())
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.player_image_placeholder)
            .error(R.drawable.player_image_placeholder)
            .into(trackImagePlayer)

        view?.findViewById<TextView>(R.id.trackNamePlayer)?.text = track.trackName
        view?.findViewById<TextView>(R.id.trackAuthor)?.text = track.artistName
        view?.findViewById<TextView>(R.id.trackDurationTextView)?.text = track.formattedDuration()
        view?.findViewById<TextView>(R.id.albNameTextView)?.text = track.collectionName
        view?.findViewById<TextView>(R.id.yearTextView)?.text = track.releaseDate?.substring(0, 4)
        view?.findViewById<TextView>(R.id.genreTextView)?.text = track.primaryGenreName
        view?.findViewById<TextView>(R.id.countryTextView)?.text = track.country

        view?.findViewById<TextView>(R.id.playerBackButton)?.setOnClickListener {
            activity?.onBackPressed()
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
            else -> {
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
            else -> {
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
        const val SELECTED_TRACK = "selectedTrack"

        fun newInstance(track: Track): PlayerFragment {
            return PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(SELECTED_TRACK, Gson().toJson(track))
                }
            }
        }
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
