package com.practicum.playlistmaker.creator

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.setApplicationContext(applicationContext)
    }
}