package com.practicum.playlistmaker.player.ui.view_model

sealed interface PlayerState {
    data object Default : PlayerState
    data object Prepared : PlayerState
    data object Playing : PlayerState
    data object Paused : PlayerState
}