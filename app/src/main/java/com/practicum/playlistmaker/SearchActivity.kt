package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private val KEY_EDIT_TEXT = "editTextValue"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        editText = findViewById(R.id.editText)

        val homeButton = findViewById<View>(R.id.searchTextView)
        homeButton.setOnClickListener {
            finish()
        }
        val buttonClear = findViewById<View>(R.id.btnClear)
        buttonClear.setOnClickListener {
            resetSearchText()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = if (s?.isNotBlank() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (savedInstanceState != null) {
            // Восстановить текст из сохраненного состояния
            val savedText = savedInstanceState.getString(KEY_EDIT_TEXT, "")
            editText.setText(savedText)
        }
    }


    private fun resetSearchText() {
        editText.text.clear()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentText = editText.text.toString()
        outState.putString(KEY_EDIT_TEXT, currentText)
    }
}