package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTabContainerBinding
import com.practicum.playlistmaker.media_library.ui.ViewPagerAdapter

class TabContainerFragment : Fragment(R.layout.fragment_tab_container) {
    private lateinit var binding: FragmentTabContainerBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabContainerBinding.bind(view)

        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.FavoriteTracksSign)
                1 -> getString(R.string.PlaylistSign)
                else -> ""
            }
        }.attach()
    }
}