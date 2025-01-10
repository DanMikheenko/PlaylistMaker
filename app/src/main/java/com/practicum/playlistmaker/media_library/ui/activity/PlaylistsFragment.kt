package com.practicum.playlistmaker.media_library.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment() {
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
        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_tabContainerFragment_to_playlistCreationFragment)
        }
    }
    companion object {
        fun newInstance(): PlaylistsFragment {
            val fragment = PlaylistsFragment()
            return fragment
        }
    }

}