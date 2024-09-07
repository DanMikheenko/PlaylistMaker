package com.practicum.playlistmaker.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.TrackAdapter

class SearchActivity : AppCompatActivity() {
    private val tracks = ArrayList<Track>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var editText: EditText
    private var lastFailedRequest = ""
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var historyLayout: LinearLayout
    private lateinit var placeholderLayout: LinearLayout
    private lateinit var connErrPlaceholder: LinearLayout
    private lateinit var recyclerView: RecyclerView

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

        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(sharedPreferences)
        trackAdapter = TrackAdapter(tracks, sharedPreferences)
        val updateBtn = findViewById<View>(R.id.updateRequestBtn)
        updateBtn.setOnClickListener {
            search()
            trackAdapter.notifyDataSetChanged()
        }

        recyclerViewHistory = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        recyclerViewHistory.adapter = TrackAdapter(getSearchHistoryTracks(), sharedPreferences)
        recyclerViewHistory.adapter?.notifyDataSetChanged()

        historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
        placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
        connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)



        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
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
                searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        if (savedInstanceState != null) {
            // Восстановить текст из сохраненного состояния
            val savedText = savedInstanceState.getString(KEY_EDIT_TEXT, "")
            editText.setText(savedText)

        }

        editText.setOnEditorActionListener { v, actionId, event ->
            tracks.clear()
            handler.removeCallbacks(searchRunnable)
            val placeholderLayout = findViewById<LinearLayout>(R.id.placeholder_layout)
            val connErrPlaceholder = findViewById<LinearLayout>(R.id.connection_error_placeholder)
            val historyLayout = findViewById<LinearLayout>(R.id.search_history_layout)
            placeholderLayout.visibility = View.GONE
            connErrPlaceholder.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                historyLayout.visibility = View.GONE
                search()
                true
            }
            false
        }
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (editText.hasFocus() && editText.text.isEmpty() && isSearchHistoryEmpty()) {
                handler.removeCallbacks(searchRunnable)
                placeholderLayout.visibility = View.GONE
                connErrPlaceholder.visibility = View.GONE
                recyclerView.visibility = View.GONE
                historyLayout.visibility = View.GONE
            }
            if (editText.hasFocus() && editText.text.isEmpty() && !isSearchHistoryEmpty()) {
                handler.removeCallbacks(searchRunnable)
                placeholderLayout.visibility = View.GONE
                connErrPlaceholder.visibility = View.GONE
                recyclerView.visibility = View.GONE
                historyLayout.visibility = View.VISIBLE
            }

        }

        val clearHistory = findViewById<View>(R.id.clearHistoryBtn)
        clearHistory.setOnClickListener {
            searchHistoryInteractor.clear()
            searchHistoryInteractor.readSearchHistory(object :
                SearchHistoryInteractor.SearchHistoryConsumer {
                override fun consume(trackHistory: List<Track>) {
                    recyclerViewHistory.adapter = TrackAdapter(trackHistory, sharedPreferences)
                }
            })
            historyLayout.visibility = View.GONE
        }


    }

    private fun resetSearchText() {
        editText.text.clear()
        handler.removeCallbacks(searchRunnable)
        placeholderLayout.visibility = View.GONE
        connErrPlaceholder.visibility = View.GONE
        historyLayout.visibility = View.VISIBLE
    }

    private fun search() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        placeholderLayout.visibility = View.GONE
        connErrPlaceholder.visibility = View.GONE
        historyLayout.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        Creator.provideTracksInteractor().searchTracks(editText.text.toString(), object :
            TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                handler.post() {
                    if (foundTracks.isEmpty()) {
                        progressBar.visibility = View.GONE
                        placeholderLayout.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                        tracks.clear()
                        tracks.addAll(foundTracks)
                        trackAdapter.notifyDataSetChanged()
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun getSearchHistoryTracks(): List<Track> {
        var searchHistoryTracks = emptyList<Track>()
        searchHistoryInteractor.readSearchHistory(object :
            SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(trackHistory: List<Track>) {
                searchHistoryTracks = trackHistory
            }
        })
        return searchHistoryTracks
    }

    private fun isSearchHistoryEmpty(): Boolean {
        var isEmpty = true
        searchHistoryInteractor.readSearchHistory(object :
            SearchHistoryInteractor.SearchHistoryConsumer {
            override fun consume(trackHistory: List<Track>) {
                if (trackHistory.isEmpty()) {
                    isEmpty = true
                } else {
                    isEmpty = false
                }
            }
        })
        return isEmpty
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

    override fun onResume() {
        super.onResume()
        recyclerViewHistory.adapter = TrackAdapter(getSearchHistoryTracks(), sharedPreferences)
        recyclerViewHistory.adapter?.notifyDataSetChanged()
    }

}