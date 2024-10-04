package com.practicum.playlistmaker.sharing.data
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(private val externalNavigation: ExternalNavigation): SharingInteractor {
    override fun shareApp() {
        externalNavigation.shareApp()
    }

    override fun openTerms() {
        externalNavigation.openTerms()
    }

    override fun openSupport() {
        externalNavigation.openSupport()
    }
}