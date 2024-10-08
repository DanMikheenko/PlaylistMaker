package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.SharingRepository

class ExternalNavigation(private val context: Context) : SharingRepository {
    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, getShareAppLink())
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openTerms() {
        val readAgreementIntent = Intent(Intent.ACTION_VIEW)
        readAgreementIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        readAgreementIntent.data = Uri.parse(getTermsLink())
        context.startActivity(readAgreementIntent)
    }

    override fun openSupport() {
        val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
        writeToSupportIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        writeToSupportIntent.data = Uri.parse("mailto:")
        writeToSupportIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(getSupportEmailData().destinationEmailAddress)
        )

        writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, getSupportEmailData().message)

        writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, getSupportEmailData().extraSubject)

        context.startActivity(writeToSupportIntent)
    }

    private fun getShareAppLink(): String {
        return context.resources.getString(R.string.shareMessage)
    }

    private fun getSupportEmailData(): EmailData {
        val emailData = EmailData(
            context.resources.getString(R.string.studentEmal),
            context.resources.getString(R.string.messageToSupportTeam),
            context.resources.getString(R.string.messageThemeToSupportTeam)
        )
        return emailData
    }

    private fun getTermsLink(): String {
        return context.resources.getString(R.string.userAgreementURI)
    }
}