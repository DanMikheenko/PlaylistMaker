package com.practicum.playlistmaker.media_library.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.media_library.ui.view_model.FavoriteTracksState
import com.practicum.playlistmaker.media_library.ui.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.OnTrackClickListener
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment(), OnTrackClickListener {
    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { _state ->
            render(_state)
        }
        viewModel.loadData()
    }

    private fun showFavoriteTracks() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.favorTracksPlaceholder.root.visibility = View.GONE
    }

    private fun showPlaceHolder() {
        binding.recyclerView.visibility = View.GONE
        binding.favorTracksPlaceholder.root.visibility = View.VISIBLE
    }


    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.ShowFavoriteTracks -> {
                binding.recyclerView.adapter =
                    TrackAdapter(state.data, this, viewLifecycleOwner.lifecycleScope)
                showFavoriteTracks()
            }

            FavoriteTracksState.ShowPlaceholder -> showPlaceHolder()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        viewModel.state.observe(viewLifecycleOwner) { _state ->
            render(_state)
        }
    }

    override fun onTrackClick(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("selectedTrack", Gson().toJson(track))
        startActivity(intent)
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            val fragment = FavoriteTracksFragment()
            return fragment
        }
    }
}