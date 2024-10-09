package com.practicum.playlistmaker.player.ui.view_model

sealed interface PlayerState {
    object Default : PlayerState
    object Prepared : PlayerState
    object Playing : PlayerState
    object Played : PlayerState
    object Paused : PlayerState
}