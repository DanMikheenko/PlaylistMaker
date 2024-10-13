package com.practicum.playlistmaker.sharing.domain

class SharingInteractorImpl(private val sharingRepository: SharingRepository): SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun openSupport() {
        sharingRepository.openSupport()
    }
}