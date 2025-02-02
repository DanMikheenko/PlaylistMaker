package com.practicum.playlistmaker.media_library.ui.activity

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistDetailsViewModel
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsDetailsState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlaylistDetailsFragment : Fragment() {
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        viewModel.playlistDuration.observe(viewLifecycleOwner) { duration ->
            binding.playlistPlayingTime.text =
                SimpleDateFormat("mm", Locale.getDefault()).format(duration)
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
                viewModel.calculatePlaylistDuration(state.data)

                Glide.with(binding.playlistImage)
                    .load(state.data.playlistImagePath)
                    .transform(RoundedCorners(10))
                    .placeholder(R.drawable.player_image_placeholder)
                    .error(R.drawable.player_image_placeholder)
                    .into(binding.playlistImage)

                binding.playlistName.text = state.data.playlistName
                binding.playlistDescription.text = state.data.description
                binding.tracksCount.text = state.data.addedTracksCount
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
}