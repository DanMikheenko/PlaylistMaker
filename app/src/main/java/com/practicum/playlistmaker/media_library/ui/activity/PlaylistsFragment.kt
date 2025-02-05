package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media_library.ui.OnPlaylistClickListener
import com.practicum.playlistmaker.media_library.ui.PlaylistAdapter
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsState
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment(), OnPlaylistClickListener{
    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_tabContainerFragment_to_playlistCreationFragment)
        }
        viewModel.state.observe(viewLifecycleOwner){_state->
            render(_state)
        }
    }

    private fun render(playlistState : PlaylistsState){
        when(playlistState){
            is PlaylistsState.ShowPlaceholder -> showPlaceholder()
            is PlaylistsState.ShowResult ->{
                binding.recyclerView.adapter = PlaylistAdapter(playlistState.data, this)
                showPlaylists()
            }
        }
    }
    private fun showPlaceholder(){
        binding.noPlaylistsCreated.root.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }
    private fun showPlaylists(){
        binding.noPlaylistsCreated.root.visibility = View.GONE
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onPlaylistClick(playlistId: String) {
        val bundle = Bundle().apply {
            putString("selectedPlaylistId", playlistId)
        }
        findNavController().navigate(R.id.tabContainerFragment_to_playlistDetailsFragment, bundle)
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            val fragment = PlaylistsFragment()
            return fragment
        }
    }
}