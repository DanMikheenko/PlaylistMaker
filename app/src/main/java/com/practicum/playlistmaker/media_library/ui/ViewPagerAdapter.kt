package com.practicum.playlistmaker.media_library.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media_library.ui.activity.FavoriteTracksFragment
import com.practicum.playlistmaker.media_library.ui.activity.PlaylistsFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {FavoriteTracksFragment.newInstance()}
            1 -> {PlaylistsFragment.newInstance()}
            else -> FavoriteTracksFragment()
        }
    }
}