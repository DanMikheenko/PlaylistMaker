package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val homeButton = findViewById<View>(R.id.searchTextView)
        homeButton.setOnClickListener{
            finish()
        }
        val buttonClear = findViewById<View>(R.id.btnClear)
        buttonClear.setOnClickListener {
            resetSearchText()
        }
    }



    private fun resetSearchText() {
        val editText = findViewById<EditText>(R.id.editText)
        editText.text.clear()
    }
}