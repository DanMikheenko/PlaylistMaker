package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        val editText = findViewById<EditText>(R.id.editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = if (s?.isNotBlank() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }



    private fun resetSearchText() {
        val editText = findViewById<EditText>(R.id.editText)
        editText.text.clear()
    }
}