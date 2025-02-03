package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.View
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistEditingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistEditingFragment : PlaylistCreationFragment() {
    private val editingViewModel: PlaylistEditingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}