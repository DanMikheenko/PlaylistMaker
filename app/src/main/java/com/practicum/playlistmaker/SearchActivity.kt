package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    private val trackService = retrofit.create(ITunesSearchAPI::class.java)
    private val tracks = ArrayList<Track>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var editText: EditText
    private var lastFailedRequest = ""
    private lateinit var searchHistory: SearchHistory

    companion object {
        const val KEY_EDIT_TEXT = "editTextValue"
        const val PLAY_LIST_MAKER_SHARE_PREFERENCES = "playListMakerSettings"
    }

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
            hideKeyboard()
            tracks.clear()
        }

        sharedPreferences = applicationContext.getSharedPreferences(
            PLAY_LIST_MAKER_SHARE_PREFERENCES, MODE_PRIVATE
        )

        searchHistory = SearchHistory(sharedPreferences)
        trackAdapter = TrackAdapter(tracks, sharedPreferences)
        val updateBtn = findViewById<View>(R.id.updateRequestBtn)
        updateBtn.setOnClickListener {
            search(lastFailedRequest)
            trackAdapter.notifyDataSetChanged()
        }

        val recyclerViewHistory = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        recyclerViewHistory.adapter =
            TrackAdapter(searchHistory.readSearchHistory(), sharedPreferences)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = trackAdapter

        recyclerView

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    buttonClear.isVisible = true
                } else {
                    buttonClear.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (savedInstanceState != null) {
            // Восстановить текст из сохраненного состояния
            val savedText = savedInstanceState.getString(KEY_EDIT_TEXT, "")
            editText.setText(savedText)
        }




        editText.setOnEditorActionListener { _, actionId, _ ->
            tracks.clear()
            val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
            val connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)
            val historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
            placeholderLayout.visibility = View.GONE
            connErrPlaceholder.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                historyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                search(editText.text.toString())
                trackAdapter.notifyDataSetChanged()
                true
            }
            false
        }
        editText.setOnFocusChangeListener { view, hasFocus ->
            val historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
            if (editText.hasFocus() && editText.text.isEmpty() && searchHistory.readSearchHistory()
                    .isNotEmpty()
            ) {
                recyclerView.visibility = View.GONE
                historyLayout.visibility = View.VISIBLE
            }

        }

        val clearHistory = findViewById<View>(R.id.clearHistoryBtn)
        clearHistory.setOnClickListener {
            searchHistory.clear()
            recyclerViewHistory.adapter =
                TrackAdapter(searchHistory.readSearchHistory(), sharedPreferences)
            val historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
            historyLayout.visibility = View.GONE
        }


    }

    private fun resetSearchText() {
        editText.text.clear()
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
        val connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)
        placeholderLayout.visibility = View.GONE
        connErrPlaceholder.visibility = View.GONE
    }

    private fun search(request: String) {

        val call = trackService.search(request)
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
        val connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)
        connErrPlaceholder.visibility = View.GONE
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val musicTracks = response.body()?.results
                    if (musicTracks != null) {
                        tracks.addAll(musicTracks)
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()) {
                        placeholderLayout.visibility = View.VISIBLE
                    }
                }
            }


            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                connErrPlaceholder.visibility = View.VISIBLE
                lastFailedRequest = request
            }
        })


    }

    override fun onSaveInstanceState(outState: Bundle) {

        val currentText = editText.text.toString()
        outState.putString(KEY_EDIT_TEXT, currentText)
        super.onSaveInstanceState(outState)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}