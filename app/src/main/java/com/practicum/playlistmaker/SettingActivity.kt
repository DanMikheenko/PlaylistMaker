package com.practicum.playlistmaker

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK
            window.navigationBarColor = Color.BLACK
        }

        val homeButton = findViewById<View>(R.id.home_button)
        homeButton.setOnClickListener{
            finish()
        }

        val shareTextView = findViewById<View>(R.id.shareTextView)
        shareTextView.setOnClickListener{
            share()
        }

        val writeTextView = findViewById<View>(R.id.writeToSpprtTextView)
        writeTextView.setOnClickListener {
            writeToSupport()
        }

        val readAgrTextView = findViewById<View>(R.id.readAgreementTextView)
        readAgrTextView.setOnClickListener {
            readUserAgreement()
        }


        val themeSwitch = findViewById<Switch>(R.id.themeSwitch)
        themeSwitch.isChecked = (applicationContext as App).isDarkThemeEnabled()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

    }
    private fun share() {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")


        val shareMessage = resources.getString(R.string.shareMessage)

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

        startActivity(Intent.createChooser(shareIntent, "Поделиться приложением через..."))
    }

    private fun writeToSupport(){
        val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
        writeToSupportIntent.data = Uri.parse("mailto:")

        val destinationEmailAdress = resources.getString(R.string.studentEmal)
        writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(destinationEmailAdress))

        val message = resources.getString(R.string.messageToSupportTeam)
        writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)

        val extraSubject = resources.getString(R.string.messageThemeToSupportTeam)
        writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)

        startActivity(writeToSupportIntent)
    }

    private fun readUserAgreement(){
        val readAgreementIntent = Intent(Intent.ACTION_VIEW)
        readAgreementIntent.data = Uri.parse(resources.getString(R.string.userAgreementURI))
        startActivity(readAgreementIntent)
    }
}