package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
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
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())

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
            search()
            trackAdapter.notifyDataSetChanged()
        }

        val recyclerViewHistory = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        recyclerViewHistory.adapter =
            TrackAdapter(searchHistory.readSearchHistory(), sharedPreferences)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = trackAdapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    buttonClear.isVisible = true
                } else {
                    buttonClear.isVisible = false
                }
                val historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
                historyLayout.visibility = View.GONE
                searchDebounce()
                recyclerView.visibility = View.VISIBLE
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
                search()
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

    private fun search() {

        val call = trackService.search(editText.text.toString())
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
        val connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        connErrPlaceholder.visibility = View.GONE
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                tracks.clear()
                progressBar.visibility = View.GONE
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
                lastFailedRequest = editText.text.toString()
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
    private val searchRunnable = Runnable { search() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}