package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media_library.ui.activity.MediaActivity
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingActivity

class MainActivity : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener{
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }


        val settingButton = findViewById<Button>(R.id.setting_button)
        settingButton.setOnClickListener{
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }


        onClick(findViewById<Button>(R.id.media_button))
    }

    override fun onClick(p0: View?) {
        p0?.setOnClickListener{
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }
    }

}