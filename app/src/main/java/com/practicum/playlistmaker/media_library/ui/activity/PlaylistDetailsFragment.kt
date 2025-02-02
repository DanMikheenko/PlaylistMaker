package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistDetailsViewModel
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsDetailsState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment(), OnTrackClickListener {
    private val viewModel by viewModel<PlaylistDetailsViewModel>()
    private lateinit var binding: FragmentPlaylistDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getString(SELECTED_PLAYLIST_ID) ?: return
        loadData(playlistId)

        val bottomSheetContainer = view.findViewById<LinearLayout>(R.id.bottomSheetPlaylistDetails)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


        binding.playlistImage.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.playlistImage.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val width = binding.playlistImage.width


                binding.playlistImage.layoutParams.height = width
                binding.playlistImage.requestLayout()
            }
        })

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            binding.recyclerViewPlaylistTracks.adapter = TrackAdapter(tracks, this, viewLifecycleOwner.lifecycleScope)
        }

        viewModel.playlistDuration.observe(viewLifecycleOwner) { duration ->
            val durationInMinutes = duration / 60000
            binding.playlistPlayingTime.text = getMinutesFormatted(durationInMinutes)
        }
    }


    private fun getMinutesFormatted(minutes: Int): String {
        return when {
            minutes % 10 == 1 && minutes % 100 != 11 -> "$minutes минута"
            minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> "$minutes минуты"
            else -> "$minutes минут"
        }
    }

    private fun getTracksCountFormatted(counts: Int): String {
        return when {
            counts % 10 == 1 && counts % 100 != 11 -> "$counts трек"
            counts % 10 in 2..4 && counts % 100 !in 12..14 -> "$counts трека"
            else -> "$counts треков"
        }
    }

    private fun loadData(selectedPlaylistId: String) {
        viewModel.loadData(selectedPlaylistId)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: PlaylistsDetailsState) {
        when (state) {
            is PlaylistsDetailsState.Error -> {}

            is PlaylistsDetailsState.Result -> {
                viewModel.loadPlaylistTracks(state.data)
                viewModel.calculatePlaylistDuration(state.data)

                Glide.with(binding.playlistImage)
                    .load(state.data.playlistImagePath)
                    .centerCrop()
                    .transform(RoundedCorners(10))
                    .placeholder(R.drawable.player_image_placeholder)
                    .error(R.drawable.player_image_placeholder)
                    .into(binding.playlistImage)

                binding.playlistName.text = state.data.playlistName
                binding.playlistDescription.text = state.data.description
                binding.tracksCount.text = getTracksCountFormatted(state.data.addedTracksCount.toInt())
            }
        }
    }

    companion object {
        const val SELECTED_PLAYLIST_ID = "selectedPlaylistId"
        fun newInstance(): PlaylistDetailsFragment {
            val fragment = PlaylistDetailsFragment()
            return fragment
        }
    }

    override fun onTrackClick(track: Track) {
        val trackJson = Gson().toJson(track)
        val bundle = Bundle().apply {
            putString("selectedTrack", trackJson)
        }
        findNavController().navigate(R.id.action_playlistDetailsFragment_to_playerFragment, bundle)
    }
}