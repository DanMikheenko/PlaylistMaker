package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val homeButton = findViewById<View>(R.id.home_button)
        homeButton.setOnClickListener{
            finish()
        }

        val shareTextView = findViewById<View>(R.id.shareTextView)
        shareTextView.setOnClickListener{
            share()
        }

    }
    private fun share() {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")


        var shareMessage = "https://practicum.yandex.ru/android-developer/"

        // Устанавливаем текст сообщения
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

        // Запускаем диалог шаринга
        startActivity(Intent.createChooser(shareIntent, "Поделиться приложением через..."))
    }
}